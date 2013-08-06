#version 150
varying vec2 texcoord;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    texcoord = vec2(gl_MultiTexCoord0.x, gl_MultiTexCoord0.y);
}