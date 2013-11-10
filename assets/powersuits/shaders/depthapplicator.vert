#version 330
uniform mat4 u_ModelViewProjectionMatrix;
layout(location = 0) in vec4 v_Vertex;
layout(location = 1) in vec4 v_TexCoord;
out vec2 texcoord;

void main() {
    gl_Position = u_ModelViewProjectionMatrix * v_Vertex;
    texcoord = vec2(v_TexCoord.x, v_TexCoord.y);
}