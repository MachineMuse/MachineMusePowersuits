#version 330

uniform sampler2D u_Depth;
in vec2 texcoord;

void main() {
	gl_FragDepth = texture2D(u_Depth, texcoord).r;
	gl_FragColor = vec4(0.0);
}