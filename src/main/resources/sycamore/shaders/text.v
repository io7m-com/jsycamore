#version 110

attribute vec2 position;
attribute vec2 uv;

uniform mat4 matrix_projection;
uniform mat4 matrix_modelview;
uniform ivec2 size;

varying vec2 out_uv;

void
main (void)
{
  float vx     = position.x * float(size.x);
  float vy     = position.y * float(size.y);
  vec4 p       = vec4(vx, vy, -1.0, 1.0);
  out_uv       = uv;
  gl_Position  = matrix_projection * matrix_modelview * p;
}

