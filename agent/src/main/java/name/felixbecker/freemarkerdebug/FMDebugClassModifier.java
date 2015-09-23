package name.felixbecker.freemarkerdebug;

import static name.felixbecker.freemarkerdebug.org.objectweb.asm.ClassReader.SKIP_FRAMES;
import static name.felixbecker.freemarkerdebug.org.objectweb.asm.Opcodes.ASM5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import name.felixbecker.freemarkerdebug.org.objectweb.asm.ClassReader;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.ClassVisitor;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.ClassWriter;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.Opcodes;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.tree.ClassNode;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.tree.MethodNode;
import name.felixbecker.freemarkerdebug.org.objectweb.asm.util.CheckClassAdapter;
import static name.felixbecker.freemarkerdebug.org.objectweb.asm.Opcodes.*;

public class FMDebugClassModifier extends ClassVisitor {

	
	public FMDebugClassModifier() {
		super(Opcodes.ASM5);
	}

	
	public byte[] patchByteCode(byte[] classByteCode, ClassLoader requestingClassLoader) throws IOException, InstantiationException, IllegalAccessException {
		final ClassReader reader = new ClassReader(classByteCode);
		return patchByteCode(reader, requestingClassLoader);
	}
	
	public byte[] patchByteCode(Class<?> classToModify, ClassLoader requestingClassLoader) throws IOException, InstantiationException, IllegalAccessException {
		final ClassReader reader = new ClassReader(classToModify.getCanonicalName());
		return patchByteCode(reader, requestingClassLoader);
	}

	
	private byte[] patchByteCode(ClassReader reader, final ClassLoader requestingClassLoader) throws IOException, InstantiationException, IllegalAccessException {
		
		// Step 1: Analyse Class
		final ClassNode classNode = new ClassNode();
		reader.accept(classNode, ClassReader.SKIP_FRAMES);
		
		
		final ClassWriter writer = new ClassWriter(/* reader for performance ref*/ reader, ClassWriter.COMPUTE_MAXS);
		
		// and instrument all methods
		FreemarkerDebugClassVisitor visitor = new FreemarkerDebugClassVisitor(ASM5, writer); 
		reader.accept(visitor, SKIP_FRAMES);
		return writer.toByteArray();

		
	}
}
