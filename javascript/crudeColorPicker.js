// simple color picker that picks up color on a click and
// displays the hex code in an alert
var CrudeColorPicker = React.createClass({

    // handle click on canvas, pick up underlying color
    handleClick: function(event) {

        // convert rgb to hex - utility function written by unknown
        function rgbToHex(r, g, b) {
            return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
        }

        // find the canvas
        var canvas = React.findDOMNode(this.refs.myCanvas);
        var ctx = canvas.getContext('2d');
        
        // need to offset event coordinates to canvas coordinates
        var offsetX = canvas.offsetLeft;
        var offsetY = canvas.offsetTop;
        
        // pick up color from canvas context
        var rgb = ctx.getImageData(event.pageX - offsetX, event.pageY - offsetY, 1, 1).data;
        var hex = rgbToHex(rgb[0], rgb[1], rgb[2]);
        alert("HEX: " + hex);
    },

    // initialize canvas with simple color gradient
    componentDidMount: function() {
        var canvas= React.findDOMNode(this.refs.myCanvas);
        var ctx = canvas.getContext("2d");
    
        var gradient = ctx.createLinearGradient(0, 0, 250, 0);
        gradient.addColorStop("0", "red");
        gradient.addColorStop("0.2", "yellow");
        gradient.addColorStop("0.4", "green");
        gradient.addColorStop("0.6", "cyan");
        gradient.addColorStop("0.8", "blue");
        gradient.addColorStop("1", "magenta");
    
        // Fill with gradient
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, 250, 200);
    },

    // render canvas with click handler
    render: function() {
        return (
            <canvas 
                ref="myCanvas" 
                id="colors" 
                width="250" 
                height="200" 
                onClick={this.handleClick}>
            </canvas>
        );
    }
});

React.render(
    <CrudeColorPicker/>,
    document.getElementById('container')
);
