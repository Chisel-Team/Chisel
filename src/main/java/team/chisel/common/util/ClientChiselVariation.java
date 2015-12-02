//package team.chisel.common.util;
//
//import team.chisel.api.block.ClientVariationData;
//import team.chisel.client.render.BlockResources;
//import team.chisel.client.render.CTMBlockResources;
//import team.chisel.client.render.IBlockResources;
//import team.chisel.common.variation.Variation;
//
//public class ClientChiselVariation extends ChiselVariation{
//
//    private IBlockResources resources;
//
//    public ClientChiselVariation(ChiselBlock parent, Variation.VariationCreator creator, ClientVariationData data){
//        super(parent, creator, data);
//    }
//
//    private ClientVariationData getClientData(){
//        return (ClientVariationData) this.data;
//    }
//
//    @Override
//    public void preInit(){
//        if (getClientData().type.isCTM()){
//            CTMBlockResources.preGenerateBlockResources(parent.getData().name, this.data.name);
//        }
//        else {
//            BlockResources.preGenerateBlockResources(parent.getData().name, this.data.name);
//        }
//    }
//
//    @Override
//    public void init(){
//        if (getClientData().type.isCTM()){
//            this.resources = CTMBlockResources.generateBlockResources(parent.getData().name, this.data.name);
//        }
//        else {
//            this.resources = BlockResources.generateBlockResources(parent.getData().name, this.data.name);
//        }
//    }
//}