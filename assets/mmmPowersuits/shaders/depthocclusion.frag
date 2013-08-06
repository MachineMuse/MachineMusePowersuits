#version 150
uniform sampler2D depth;
uniform sampler2D occlusion;
uniform sampler2D texture;
varying vec2 texcoord;

void main() {
    float depth = texture(depth, texcoord).r - 0.001;
    float occlusion = texture(occlusion, texcoord).r;
    vec4 color = vec4(0.0);
    float writedepth = occlusion;
    if(depth <= occlusion) {
        color = texture(texture, texcoord);
//        color = vec4(1.0);
        writedepth = depth;
    }
//    color = vec4(vec3(occlusion - depth),1);
	gl_FragDepth = writedepth;
	gl_FragColor = color;
}