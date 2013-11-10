#version 410
uniform sampler2D depth;
in vec2 texcoord;

void main() {
	gl_FragDepth = texture(depth, texcoord).r;
	gl_FragColor = vec4(0.0);
}