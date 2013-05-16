#version 400
uniform vec2 u_Scale;
uniform sampler2D u_Texture0;

const float gaussFilter[14] = float[14](
    -3.0,	0.015625,
	-2.0,	0.09375,
	-1.0,	0.234375,
	0.0,	0.3125,
	1.0,	0.234375,
	2.0,	0.09375,
	3.0,	0.015625
);

void main() {
	vec4 color = vec4(0.0);
	float wt = 0.0;
	for( int i = 0; i < 7; i++ )
	{
	    vec4 pix = texture2D( u_Texture0,
	        vec2(gl_TexCoord[0].x+gaussFilter[i*2]*u_Scale.x, gl_TexCoord[0].y+gaussFilter[i*2]*u_Scale.y )
	    );
		color += pix*gaussFilter[i*2+1] * pix.w*1.5;
	}
	gl_FragColor = color;
}