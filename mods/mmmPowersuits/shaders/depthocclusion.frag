#version 150
uniform sampler2D depth;
uniform sampler2D occlusion;
uniform sampler2D texture;

void main() {
    vec2 coords = vec2(gl_TexCoord[0].x, gl_TexCoord[0].y);
    float depth = texture(depth, coords).r - 0.001;
    float occlusion = texture(occlusion, coords).r;
    vec4 color = vec4(0.0);
    float writedepth = occlusion;
    if(depth <= occlusion) {
        color = texture(texture, coords);
//        color = vec4(1.0);
        writedepth = depth;
    }
//    color = vec4(vec3(occlusion - depth),1);
	gl_FragDepth = writedepth;
	gl_FragColor = color;
}