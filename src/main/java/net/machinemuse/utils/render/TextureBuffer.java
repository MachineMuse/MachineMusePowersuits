package net.machinemuse.utils.render;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

/**
 * Ported to Java by lehjr on 12/10/16.
 */
public class TextureBuffer {
    // Allocate IDs for a FBO, colour buffer, and depth buffer
    int framebufferID = EXTFramebufferObject.glGenFramebuffersEXT();
    int colorTextureID = GL11.glGenTextures();
    int depthRenderBufferID = GL11.glGenTextures();
    int texDimension;

    public TextureBuffer(int texDimension) {
        this.texDimension = texDimension;

        // check if FBO is enabled
        if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            throw new RuntimeException("Framebuffers not supported!");
        }

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, texDimension, texDimension, 0, GL11.GL_RGBA, GL11.GL_INT, (ByteBuffer)null);
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthRenderBufferID);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, texDimension, texDimension, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_INT, (ByteBuffer)null);
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, GL11.GL_TEXTURE_2D, depthRenderBufferID, 0);

        // Check the status and throw an exception if it didn't initialize properly
        switch(EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT)){
            case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
                break;
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
            case EXTFramebufferObject.GL_FRAMEBUFFER_UNSUPPORTED_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_UNSUPPORTED_EXT exception");
            default:
                throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + "<insert some relavent info here>");
        }

        // Bind the display buffer
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
    }

    public void bindRead() {
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
    }

    public void unbindRead() {
        GL11.glPopAttrib();
    }

    public void bindWrite() {
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);
        GL11.glViewport(0, 0, texDimension, texDimension);
    }

    public void unbindWrite() {
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        GL11.glPopAttrib();
    }

    public void clear() {
        bindWrite();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        unbindWrite();
    }
}