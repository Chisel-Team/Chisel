package info.jbcs.minecraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderBlocksEldritch extends RenderBlocks
{

    RenderBlocksEldritch()
    {
        super();
    }

    static double displacementMap[] = {
            +0.1165, -0.0352, -0.1208, +0.0906, -0.0295, +0.0302, -0.0973, +0.0548,
            -0.1077, +0.0662, -0.0932, -0.0925, -0.1161, +0.0073, -0.1084, +0.1136,
            +0.0307, +0.0316, -0.1066, -0.0237, +0.0614, -0.0864, +0.0837, +0.1218,
            +0.0502, -0.0529, -0.0942, +0.1209, -0.0418, +0.1035, +0.0066, -0.0255,
            +0.0830, -0.0361, +0.0965, +0.0646, +0.1124, +0.0890, -0.0037, +0.0467,
            +0.1025, +0.1161, +0.0014, +0.0365, +0.0755, -0.0025, +0.0739, +0.0656,
            +0.0571, -0.0813, -0.0062, -0.0904, -0.0005, +0.0612, -0.0227, +0.0138,
            -0.0899, -0.0019, -0.1047, +0.1191, -0.1197, -0.1123, +0.0999, +0.0638,
            +0.0865, +0.0029, -0.0084, +0.1132, -0.0433, +0.0115, +0.0850, +0.0439,
            +0.0748, +0.0460, -0.0441, +0.0839, +0.0162, -0.0229, +0.0774, -0.0647,
            -0.0636, -0.0709, -0.0719, -0.0194, +0.1070, -0.0466, -0.1200, -0.0996,
            -0.0298, -0.1035, -0.0006, +0.0530, -0.0939, -0.1210, +0.0176, +0.0924,
            +0.0343, -0.0777, +0.1050, -0.0729, -0.0062, -0.1107, -0.0434, -0.0042,
            +0.0232, +0.0269, +0.0709, -0.0388, +0.0342, +0.0240, -0.0192, +0.0491,
            +0.0017, -0.1240, +0.0687, -0.1160, +0.1123, -0.0332, -0.0260, +0.0476,
            -0.0049, -0.0893, -0.0243, -0.0371, -0.0230, +0.0656, +0.1080, +0.0781,
            -0.0648, +0.0082, -0.1086, -0.0451, -0.0594, -0.1155, -0.0623, -0.0152,
            +0.0131, -0.0754, +0.0607, +0.0876, -0.0657, +0.0366, -0.0981, +0.1164,
            -0.0623, +0.0181, -0.0475, +0.0722, -0.0293, +0.0486, -0.0195, +0.1041,
            +0.1011, +0.0042, -0.0045, -0.1031, +0.0110, +0.0058, +0.0083, -0.0782,
            -0.1003, -0.0082, -0.0304, -0.0971, -0.0097, -0.0208, -0.1059, -0.0239,
            -0.0385, +0.0505, +0.0537, -0.1098, -0.0022, -0.0165, -0.0027, -0.0208,
            +0.0305, +0.0592, +0.0301, +0.0055, +0.1138, -0.0338, -0.0901, +0.1015,
            +0.0061, +0.0861, -0.1057, -0.0889, -0.0972, +0.0783, -0.1052, -0.0932,
            -0.1103, +0.0497, +0.1222, -0.0146, +0.0632, -0.0240, -0.0193, +0.0676,
            -0.1128, -0.0886, +0.0926, +0.0538, -0.0175, -0.0966, -0.0369, +0.1124,
            -0.0820, -0.1013, -0.0836, -0.0981, -0.0502, +0.0573, +0.0616, -0.0458,
            +0.0294, +0.0108, -0.0782, +0.0692, +0.0234, -0.0178, -0.0171, -0.0505,
            -0.0313, +0.0538, +0.0462, -0.0758, +0.0556, -0.0023, -0.1086, -0.0100,
            -0.0897, -0.0409, -0.1161, +0.0212, +0.1229, -0.0945, +0.0341, +0.1238,
            +0.0893, -0.1189, -0.1098, -0.0125, +0.1137, -0.0202, +0.0239, -0.0408,
            +0.0920, +0.0777, -0.0085, -0.0572, -0.0293, -0.0131, +0.0272, +0.0991,
    };

    static int dmap1 = displacementMap.length * 1 / 6;
    static int dmap2 = displacementMap.length * 2 / 6;
    static int dmap3 = displacementMap.length * 3 / 6;
    static int dmap4 = displacementMap.length * 4 / 6;
    static int dmap5 = displacementMap.length * 5 / 6;
    static int dmap = displacementMap.length;

    Tessellator tessellator;
    double[] X = new double[14];
    double[] Y = new double[14];
    double[] Z = new double[14];
    double[] U = new double[14];
    double[] V = new double[14];
    int[] L = new int[14];
    float[] R = new float[14];
    float[] G = new float[14];
    float[] B = new float[14];

    @Override
    public boolean renderStandardBlock(Block block, int x, int y, int z)
    {
        int index = x + y * 34573 + z * 32465781;
        if(index < 0) index = -index;

        X[0] = x;
        Z[0] = z;
        Y[0] = y;

        X[1] = x;
        Z[1] = z;
        Y[1] = y + 1;

        X[2] = x + 1;
        Z[2] = z;
        Y[2] = y + 1;

        X[3] = x + 1;
        Z[3] = z;
        Y[3] = y;

        X[4] = x;
        Z[4] = z + 1;
        Y[4] = y;

        X[5] = x;
        Z[5] = z + 1;
        Y[5] = y + 1;

        X[6] = x + 1;
        Z[6] = z + 1;
        Y[6] = y + 1;

        X[7] = x + 1;
        Z[7] = z + 1;
        Y[7] = y;

        X[8] = x + 0.5 + displacementMap[(index + 0) % dmap];
        Z[8] = z - displacementMap[(index + 10) % dmap];
        Y[8] = y + 0.5 + displacementMap[(index + 20) % dmap];

        X[9] = x - displacementMap[(index + 0 + dmap1) % dmap];
        Z[9] = z + 0.5 + displacementMap[(index + 10 + dmap1) % dmap];
        Y[9] = y + 0.5 + displacementMap[(index + 20 + dmap1) % dmap];

        X[10] = x + 0.5 + displacementMap[(index + 0 + dmap2) % dmap];
        Z[10] = z + 1 + displacementMap[(index + 10 + dmap2) % dmap];
        Y[10] = y + 0.5 + displacementMap[(index + 20 + dmap2) % dmap];

        X[11] = x + 1 + displacementMap[(index + 0 + dmap3) % dmap];
        Z[11] = z + 0.5 + displacementMap[(index + 10 + dmap3) % dmap];
        Y[11] = y + 0.5 + displacementMap[(index + 20 + dmap3) % dmap];

        X[12] = x + 0.5 + displacementMap[(index + 0 + dmap4) % dmap];
        Z[12] = z + 0.5 + displacementMap[(index + 10 + dmap4) % dmap];
        Y[12] = y + 1;//+displacementMap[(index+20+dmap4)%dmap];

        X[13] = x + 0.5 + displacementMap[(index + 0 + dmap5) % dmap];
        Z[13] = z + 0.5 + displacementMap[(index + 10 + dmap5) % dmap];
        Y[13] = y + 0;//+displacementMap[(index+20+dmap5)%dmap];

        tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        return super.renderStandardBlock(block, x, y, z);
    }

    void setupSides(IIcon icon, int a, int b, int c, int d, int e, int ta, int tb, int tc, int td)
    {
        L[a] = brightnessBottomLeft;
        L[b] = brightnessBottomRight;
        L[c] = brightnessTopRight;
        L[d] = brightnessTopLeft;
        L[e] = (brightnessBottomLeft + brightnessTopLeft + brightnessTopRight + brightnessBottomRight) / 4;

        R[a] = colorRedBottomLeft;
        R[b] = colorRedBottomRight;
        R[c] = colorRedTopRight;
        R[d] = colorRedTopLeft;
        R[e] = (colorRedBottomLeft + colorRedTopLeft + colorRedTopRight + colorRedBottomRight) / 4;

        G[a] = colorGreenBottomLeft;
        G[b] = colorGreenBottomRight;
        G[c] = colorGreenTopRight;
        G[d] = colorGreenTopLeft;
        G[e] = (colorGreenBottomLeft + colorGreenTopLeft + colorGreenTopRight + colorGreenBottomRight) / 4;

        B[a] = colorBlueBottomLeft;
        B[b] = colorBlueBottomRight;
        B[c] = colorBlueTopRight;
        B[d] = colorBlueTopLeft;
        B[e] = (colorBlueBottomLeft + colorBlueTopLeft + colorBlueTopRight + colorBlueBottomRight) / 4;

        double u0 = icon.getMaxU();
        double u1 = icon.getMinU();
        double v0 = icon.getMaxV();
        double v1 = icon.getMinV();

        U[ta] = u0;
        U[tb] = u0;
        U[tc] = u1;
        U[td] = u1;
        U[e] = icon.getInterpolatedU(8.0D);

        V[ta] = v0;
        V[tb] = v1;
        V[tc] = v1;
        V[td] = v0;
        V[e] = icon.getInterpolatedV(8.0D);
    }

    void vert(int index)
    {
        if(enableAO)
        {
            tessellator.setColorOpaque_F(R[index], G[index], B[index]);
            tessellator.setBrightness(L[index]);
        }

        tessellator.addVertexWithUV(X[index], Y[index], Z[index], U[index], V[index]);
    }

    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 1, 0, 4, 5, 9, 4, 5, 1, 0);
        vert(1);
        vert(0);
        vert(9);
        vert(9);
        vert(0);
        vert(4);
        vert(9);
        vert(9);
        vert(4);
        vert(5);
        vert(9);
        vert(9);
        vert(5);
        vert(1);
        vert(9);
        vert(9);
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 3, 2, 6, 7, 11, 3, 2, 6, 7);
        vert(6);
        vert(7);
        vert(11);
        vert(11);
        vert(7);
        vert(3);
        vert(11);
        vert(11);
        vert(3);
        vert(2);
        vert(11);
        vert(11);
        vert(2);
        vert(6);
        vert(11);
        vert(11);

    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 2, 3, 0, 1, 8, 0, 1, 2, 3);
        vert(0);
        vert(1);
        vert(8);
        vert(8);
        vert(1);
        vert(2);
        vert(8);
        vert(8);
        vert(2);
        vert(3);
        vert(8);
        vert(8);
        vert(3);
        vert(0);
        vert(8);
        vert(8);
    }


    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 4, 7, 6, 5, 10, 7, 6, 5, 4);
        vert(5);
        vert(4);
        vert(10);
        vert(10);
        vert(6);
        vert(5);
        vert(10);
        vert(10);
        vert(7);
        vert(6);
        vert(10);
        vert(10);
        vert(4);
        vert(7);
        vert(10);
        vert(10);
    }


    @Override
    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 0, 3, 7, 4, 13, 0, 3, 7, 4);
        vert(0);
        vert(3);
        vert(13);
        vert(13);
        vert(3);
        vert(7);
        vert(13);
        vert(13);
        vert(7);
        vert(4);
        vert(13);
        vert(13);
        vert(4);
        vert(0);
        vert(13);
        vert(13);
    }

    @Override
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon)
    {
        setupSides(icon, 2, 1, 5, 6, 12, 2, 1, 5, 6);

        vert(2);
        vert(1);
        vert(12);
        vert(12);
        vert(1);
        vert(5);
        vert(12);
        vert(12);
        vert(5);
        vert(6);
        vert(12);
        vert(12);
        vert(6);
        vert(2);
        vert(12);
        vert(12);
    }

}
