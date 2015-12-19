package team.chisel.client;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.block.ChiselRecipe;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.common.util.json.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Block Face data for a block
 */
public class BlockFaceData {

    private VariationFaceData[] variationData;

    public BlockFaceData(ClientVariationData[] clientVariationData){
        variationData = new VariationFaceData[clientVariationData.length];
        for (int i = 0 ; i < clientVariationData.length ; i++ ){
            variationData[i] = new VariationFaceData(clientVariationData[i]);
        }
    }

    public BlockFaceData(VariationData[] clientVariationData){
        variationData = new VariationFaceData[clientVariationData.length];
        for (int i = 0 ; i < clientVariationData.length ; i++ ){
            variationData[i] = new VariationFaceData((ClientVariationData)clientVariationData[i]);
        }
    }

    public VariationFaceData getForMeta(int meta){
        try {
            return this.variationData[meta];
        } catch (ArrayIndexOutOfBoundsException exception){
            Chisel.debug("Meta "+meta+" out of bounds");
            return this.variationData[meta];
        }
    }



    public static class VariationFaceData {

        /**
         * The Default chisel texture for the sides of this block
         */
        public IChiselFace defaultFace;

        /**
         * The chisel texture for the sides of this block
         */
        private Map<EnumFacing, IChiselFace> sideOverrides;

        private List<IBlockRenderType> typesUsed;

        public VariationFaceData(ClientVariationData data){
            this(JsonHelper.getOrCreateFace(data.defaultFace), transformMap(data.sideOverrides));
        }

        private static Map<EnumFacing, IChiselFace> transformMap(Map<EnumFacing, ResourceLocation> mapIn){
            Map<EnumFacing, IChiselFace> map = new HashMap<>();
            for (EnumFacing facing : mapIn.keySet()){
                map.put(facing, JsonHelper.getOrCreateFace(mapIn.get(facing)));
            }
            return map;
        }

        public VariationFaceData(IChiselFace defaultFace, Map<EnumFacing, IChiselFace> sideOverrides){
            this.defaultFace = defaultFace;
            this.sideOverrides = sideOverrides;
            this.typesUsed = new ArrayList<IBlockRenderType>();
            for (IChiselFace face : sideOverrides.values()){
                for (IChiselTexture texture : face.getTextureList()) {
                    if (!typesUsed.contains(texture.getBlockRenderType())){
                        typesUsed.add(texture.getBlockRenderType());
                    }
                }
            }
            for (IChiselTexture texture : defaultFace.getTextureList()) {
                if (!typesUsed.contains(texture.getBlockRenderType())) {
                    typesUsed.add(texture.getBlockRenderType());
                }
            }
        }

        public List<IChiselFace> getAllFaces(){
            List<IChiselFace> faces = new ArrayList<>();
            faces.add(defaultFace);
            faces.addAll(sideOverrides.values());
            return faces;
        }

        public void setDefaultFace(ChiselFace face){
            this.defaultFace = face;
        }

        public void setFace(EnumFacing facing, ChiselFace face){
            sideOverrides.put(facing, face);
        }

        public List<IBlockRenderType> getTypesUsed() {
            return this.typesUsed;
        }

        public IChiselFace getFaceForSide(EnumFacing facing){
            if (facing == null){
                return defaultFace;
            }
            else if (sideOverrides.containsKey(facing)){
                return sideOverrides.get(facing);
            }
            else {
                return defaultFace;
            }
        }

    }
}
