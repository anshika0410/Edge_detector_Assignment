"use strict";
// Get a reference to the image element in our HTML
const imageView = document.getElementById('processed-image');
// Get a reference to the paragraph element for stats
const statsOverlay = document.getElementById('stats-overlay');
// Set the source of the image to our sample frame
imageView.src = 'sample_frame.png';
// Update the stats text
statsOverlay.innerText = 'Frame Stats | Resolution: 720x1280 | FPS: (simulated) 20';
