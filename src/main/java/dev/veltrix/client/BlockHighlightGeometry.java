package dev.veltrix.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4fc;

public final class BlockHighlightGeometry {
    private BlockHighlightGeometry() {}

    public static void renderFill(VertexConsumer out, PoseStack.Pose pose, VoxelShape shape, int argb) {
        float a = ((argb >>> 24) & 255) / 255.0f;
        float r = ((argb >>> 16) & 255) / 255.0f;
        float g = ((argb >>> 8) & 255) / 255.0f;
        float b = (argb & 255) / 255.0f;

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double e = 0.003;
            float x0 = (float) (minX - e), y0 = (float) (minY - e), z0 = (float) (minZ - e);
            float x1 = (float) (maxX + e), y1 = (float) (maxY + e), z1 = (float) (maxZ + e);
            quad(out, pose, x0,y0,z1, x1,y0,z1, x1,y0,z0, x0,y0,z0, r,g,b,a);
            quad(out, pose, x0,y1,z1, x1,y1,z1, x1,y1,z0, x0,y1,z0, r,g,b,a);
            quad(out, pose, x1,y0,z0, x0,y0,z0, x0,y1,z0, x1,y1,z0, r,g,b,a);
            quad(out, pose, x0,y0,z1, x1,y0,z1, x1,y1,z1, x0,y1,z1, r,g,b,a);
            quad(out, pose, x0,y0,z0, x0,y0,z1, x0,y1,z1, x0,y1,z0, r,g,b,a);
            quad(out, pose, x1,y0,z1, x1,y0,z0, x1,y1,z0, x1,y1,z1, r,g,b,a);
        });
    }

    private static void quad(VertexConsumer out, PoseStack.Pose pose,
                             float x0,float y0,float z0, float x1,float y1,float z1,
                             float x2,float y2,float z2, float x3,float y3,float z3,
                             float r,float g,float b,float a) {
        Matrix4fc matrix = pose.pose();
        out.addVertex(matrix, x0,y0,z0).setColor(r,g,b,a);
        out.addVertex(matrix, x1,y1,z1).setColor(r,g,b,a);
        out.addVertex(matrix, x2,y2,z2).setColor(r,g,b,a);
        out.addVertex(matrix, x3,y3,z3).setColor(r,g,b,a);
    }
}
