// This attribute will receive the vertex positions
attribute vec4 a_Position;
// This attribute will receive the texture coordinates
attribute vec2 a_TexCoordinate;
// This varying will pass the texture coordinates to the fragment shader
varying vec2 v_TexCoordinate;

void main() {
    // Pass the texture coordinate to the fragment shader
    v_TexCoordinate = a_TexCoordinate;
    // Set the final position of the vertex
    gl_Position = a_Position;
}