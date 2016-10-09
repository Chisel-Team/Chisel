package team.chisel.common.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import static org.objectweb.asm.Opcodes.*;

import net.minecraft.launchwrapper.IClassTransformer;

public class ChiselTransformer implements IClassTransformer {

    private static final String BLOCK_CLASS = "net.minecraft.block.Block";
    private static final String EXTENDED_STATE_METHOD_NAME = "getExtendedState";
    private static final String CAN_RENDER_IN_LAYER_METHOD_NAME = "canRenderInLayer";
    private static final String CAN_RENDER_IN_LAYER_METHOD_DESC = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z";

    private static final String WRAPPER_CLASS_NAME = "team/chisel/client/ChiselExtendedState";
    private static final String WRAPPER_CLASS_CONSTRUCTOR_NAME = "<init>";
    private static final String WRAPPER_CLASS_CONSTRUCTOR_DESC = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)V";
    
    private static final String CHISEL_METHODS_CLASS_NAME = "team/chisel/common/asm/ChiselCoreMethods";
    
    private static final String CHISEL_METHODS_LAYER_NAME = "canRenderInLayer";
    private static final String CHISEL_METHODS_LAYER_DESC = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z";
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals(BLOCK_CLASS)) {
            System.out.println("Transforming Class [" + transformedName + "], Method [" + EXTENDED_STATE_METHOD_NAME + "]");

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            Iterator<MethodNode> methods = classNode.methods.iterator();

            while (methods.hasNext()) {
                MethodNode m = methods.next();
                if (m.name.equals(EXTENDED_STATE_METHOD_NAME)) {
                    for (int i = 0; i < m.instructions.size(); i++) {
                        AbstractInsnNode next = m.instructions.get(i);
                        // Find return statement
                        if (next instanceof InsnNode && ((InsnNode)next).getOpcode() == ARETURN) {
                            InsnList toAdd = new InsnList();
                            
                            // FIXME find a better way to do this, might not always be an ALOAD
                            // Grab lvt ID of the current object on the stack
                            AbstractInsnNode load = m.instructions.get(i - 1);
                            int var = ((VarInsnNode)load).var;
                            
                            // Wrap the object that was about to be returned in our own object
                            // This allows multiple hooks here to exist. If someone else adds a wrapper, we will wrap that wrapper :D
                            toAdd.add(new InsnNode(POP));
                            toAdd.add(new TypeInsnNode(NEW, WRAPPER_CLASS_NAME));
                            toAdd.add(new InsnNode(DUP));
                            // Put the old value back on, to be wrapped
                            toAdd.add(new VarInsnNode(ALOAD, var));
                            toAdd.add(new VarInsnNode(ALOAD, 2));
                            toAdd.add(new VarInsnNode(ALOAD, 3));
                            toAdd.add(new MethodInsnNode(INVOKESPECIAL, WRAPPER_CLASS_NAME, WRAPPER_CLASS_CONSTRUCTOR_NAME, WRAPPER_CLASS_CONSTRUCTOR_DESC, false));

                            m.instructions.insertBefore(next, toAdd);
                            break;
                        }
                    }
                } else if (m.name.equals(CAN_RENDER_IN_LAYER_METHOD_NAME) && m.desc.equals(CAN_RENDER_IN_LAYER_METHOD_DESC)) {
                    m.instructions.clear(); // FIXME probably bad

                    m.instructions.add(new VarInsnNode(ALOAD, 1));
                    m.instructions.add(new VarInsnNode(ALOAD, 2));
                    m.instructions.add(new MethodInsnNode(INVOKESTATIC, CHISEL_METHODS_CLASS_NAME, CHISEL_METHODS_LAYER_NAME, CHISEL_METHODS_LAYER_DESC, false));
                    m.instructions.add(new InsnNode(IRETURN));
                }
            }

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(cw);
            System.out.println("Transforming " + transformedName + " Finished.");
            return cw.toByteArray();
        }
        
        return basicClass;
    }
}
