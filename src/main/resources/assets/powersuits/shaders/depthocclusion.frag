#version 330
uniform sampler2D u_Occlusion;
uniform sampler2D u_Texture;
uniform sampler2D u_Depth;
in vec2 texcoord;

void main() {
    float occlusion = texture2D(u_Occlusion, texcoord).r;
    float depth = texture2D(u_Depth, texcoord).r - 0.001;
    vec4 color = vec4(0.0);
    float writedepth = occlusion;
    if(depth <= occlusion) {
        color = texture2D(u_Texture, texcoord);
//        color = vec4(1.0);
        writedepth = depth;
    }
//    color = vec4(vec3(occlusion - depth),1);
	gl_FragDepth = writedepth;
	gl_FragColor = color;
}