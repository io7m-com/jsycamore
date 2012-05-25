#version 110

uniform sampler2D texture;
uniform float alpha;
uniform vec4 color;

varying vec2 out_uv;

void
main (void)
{
  vec4 texel   = texture2D(texture, out_uv);
  gl_FragColor = vec4(texel.g * color.rgb, texel.a * alpha);
}

