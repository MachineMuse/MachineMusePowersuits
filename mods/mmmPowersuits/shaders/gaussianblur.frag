varying vec2 v_Coordinates;

uniform vec2 u_Scale;
uniform sampler2D u_Texture0;

const vec2 gaussFilter[7] = {
	-3.0,	0.015625,
	-2.0,	0.09375,
	-1.0,	0.234375,
	0.0,	0.3125,
	1.0,	0.234375,
	2.0,	0.09375,
	3.0,	0.015625
};

void main() {
	vec4 color = 0.0;
	for( int i = 0; i < 7; i++ )
	{
		color += texture2D( u_Texture0,
		    vec2(
		        v_Coordinates.x+gaussFilter[i].x*u_Scale.x,
		        v_Coordinates.y+gaussFilter[i].x*u_Scale.y )
		     )*gaussFilter[i].y;
	}

	gl_FragColor = color;
}