precision mediump float;

// This uniform will be our camera texture
uniform sampler2D u_Texture;
// This receives the texture coordinate from the vertex shader
varying vec2 v_TexCoordinate;

void main() {
    // Sample the color from the texture at the given coordinate
    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);
}