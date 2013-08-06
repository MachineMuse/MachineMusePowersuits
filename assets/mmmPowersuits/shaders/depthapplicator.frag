#version 150
uniform sampler2D depth;
varying vec2 texcoord;

void main() {
	gl_FragDepth = texture(depth, texcoord).r;
	gl_FragColor = vec4(0.0);
}