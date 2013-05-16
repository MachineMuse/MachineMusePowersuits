#version 400
uniform sampler2D depth;

void main() {
    vec2 coords = vec2(gl_TexCoord[0].x, gl_TexCoord[0].y);
	gl_FragDepth = texture2D(depth, coords).r;
	gl_FragColor = vec4(0.0);
}