package dev.veltrix.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class BlockHighlightRenderer {
    private static boolean initialized;
    private static double smoothX, smoothY, smoothZ;
    private static long lastFrameTime;

    private BlockHighlightRenderer() {}

    public static void register() {
        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, state) -> {
            if (!BlockHighlightSettings.enabled || state == null) {
                initialized = false;
                return true;
            }

            BlockPos pos = state.pos();
            VoxelShape shape = state.shape();
            if (pos == null || shape == null || shape.isEmpty()) {
                initialized = false;
                return true;
            }

            PoseStack matrices = context.poseStack();
            if (matrices == null || context.levelState() == null
                    || context.levelState().cameraRenderState == null
                    || context.submitNodeCollector() == null) {
                return true;
            }

            Vec3 camera = context.levelState().cameraRenderState.pos;
            if (camera == null) return true;

            double targetX = pos.getX(), targetY = pos.getY(), targetZ = pos.getZ();
            long now = System.currentTimeMillis();
            if (!BlockHighlightSettings.smooth || !initialized) {
                smoothX = targetX;
                smoothY = targetY;
                smoothZ = targetZ;
                initialized = true;
            } else {
                long dt = Math.max(1L, Math.min(100L, now - lastFrameTime));
                double distance = square(targetX - smoothX) + square(targetY - smoothY) + square(targetZ - smoothZ);
                if (distance > 25.0) {
                    smoothX = targetX; smoothY = targetY; smoothZ = targetZ;
                } else {
                    double amount = 1.0 - Math.exp(-0.12 * dt);
                    smoothX += (targetX - smoothX) * amount;
                    smoothY += (targetY - smoothY) * amount;
                    smoothZ += (targetZ - smoothZ) * amount;
                }
            }
            lastFrameTime = now;

            matrices.pushPose();
            matrices.translate(smoothX - camera.x, smoothY - camera.y, smoothZ - camera.z);
            int color = BlockHighlightSettings.currentOutlineColor(now);
            context.submitNodeCollector().submitShapeOutline(
                    matrices, shape, RenderTypes.lines(), color, BlockHighlightSettings.outlineWidth, false
            );

            if (BlockHighlightSettings.fillEnabled) {
                context.submitNodeCollector().submitCustomGeometry(
                        matrices,
                        RenderTypes.debugFilledBox(),
                        (pose, consumer) -> BlockHighlightGeometry.renderFill(
                                consumer, pose, shape, BlockHighlightSettings.fillColor
                        )
                );
            }
            matrices.popPose();
            return false;
        });
    }

    private static double square(double value) { return value * value; }
}
