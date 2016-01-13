package team.chisel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;
import team.chisel.api.block.ClientVariationData;
import team.chisel.api.block.VariationData;
import team.chisel.api.render.IBlockRenderType;
import team.chisel.api.render.IChiselFace;
import team.chisel.api.render.IChiselTexture;
import team.chisel.common.util.json.JsonHelper;

/**
 * Block Face data for a block
 */
public class BlockFaceData {

    private VariationFaceData[] variationData;

    private List<EnumWorldBlockLayer> layers;

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
            return this.variationData[0];
        }
    }

    public boolean isValid(EnumWorldBlockLayer layer){
        if (this.layers == null){
            this.layers = new ArrayList<EnumWorldBlockLayer>();
            for (VariationFaceData data : this.variationData){
                for (IChiselFace face : data.getAllFaces()){
                    if (!this.layers.contains(face.getLayer())){
                        this.layers.add(face.getLayer());
                    }
                }
            }
        }
        return this.layers.contains(layer);
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
            this(JsonHelper.getOrCreateFace(toBlock(data.defaultFace)), transformMap(data.sideOverrides));
        }

        private static Map<EnumFacing, IChiselFace> transformMap(Map<EnumFacing, ResourceLocation> mapIn){
            Map<EnumFacing, IChiselFace> map = new HashMap<>();
            for (Entry<EnumFacing, ResourceLocation> e : mapIn.entrySet()){
                map.put(e.getKey(), JsonHelper.getOrCreateFace(toBlock(e.getValue())));
            }
            return map;
        }
        
        private static ResourceLocation toBlock(ResourceLocation loc) {
            return new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath() + JsonHelper.FACE_EXTENSION);
        }

        public VariationFaceData(IChiselFace defaultFace, Map<EnumFacing, IChiselFace> sideOverrides){
            this.defaultFace = defaultFace;
            this.sideOverrides = sideOverrides;
            this.typesUsed = new ArrayList<IBlockRenderType>();
            for (IChiselFace face : sideOverrides.values()){
                for (IChiselTexture<?> texture : face.getTextureList()) {
                    if (!typesUsed.contains(texture.getType())){
                        typesUsed.add(texture.getType());
                    }
                }
            }
            for (IChiselTexture<?> texture : defaultFace.getTextureList()) {
                if (!typesUsed.contains(texture.getType())) {
                    typesUsed.add(texture.getType());
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
