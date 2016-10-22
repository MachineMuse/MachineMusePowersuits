package net.machinemuse.utils.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:18 PM, 5/15/13
 *
 * Ported to Java by lehjr on 10/19/16.
 */
public class TextureBuffer {
    // Allocate IDs for a FBO, colour buffer, and depth buffer
    final int framebufferID = glGenFramebuffersEXT();
    final int colorTextureID = GL11.glGenTextures();
    final int  depthRenderBufferID = GL11.glGenTextures();
    final int texDimension;

    public TextureBuffer(int texDimension) {
        this.texDimension = texDimension;

        // check if FBO is enabled
        if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            throw new RuntimeException("Framebuffers not supported!");
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

        glBindTexture(GL_TEXTURE_2D, colorTextureID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texDimension, texDimension, 0, GL_RGBA, GL_INT, (ByteBuffer)null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0);

        glBindTexture(GL_TEXTURE_2D, depthRenderBufferID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, texDimension, texDimension, 0, GL_DEPTH_COMPONENT, GL_INT, (ByteBuffer) null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D, depthRenderBufferID, 0);

        // Check the status and throw an exception if it didn't initialize properly
        switch(glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT)) {
            case GL_FRAMEBUFFER_COMPLETE_EXT:
                break;
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception");
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception");
            case GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception");
            case GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception");
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception");
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception");
            case GL_FRAMEBUFFER_UNSUPPORTED_EXT:
                throw new RuntimeException("FrameBuffer: " + framebufferID + ", has caused a GL_FRAMEBUFFER_UNSUPPORTED_EXT exception");
            default:
                throw new RuntimeException("Unexpected reply from glCheckFramebufferStatusEXT: " + "<insert some relavent info here>");
        }

        // Bind the display buffer
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void bindRead() {
        glPushAttrib(GL_TEXTURE_BIT);
        glBindTexture(GL_TEXTURE_2D, colorTextureID);
    }

    public void unbindRead() {
        glPopAttrib();
    }

    public void bindWrite() {
        glPushAttrib(GL_VIEWPORT_BIT);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
        glViewport(0, 0, texDimension, texDimension);
    }

    public void unbindWrite() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        glPopAttrib();
    }

    public void clear() {
        bindWrite();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        unbindWrite();
    }
}
