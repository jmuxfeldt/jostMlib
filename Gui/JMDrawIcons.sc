JMDrawIcons {

	*initClass{
		\DrawIcon.asClass.notNil.if{
			Class.initClassTree(DrawIcon);
			this.getFuncs.keysValuesDo{|k,v|
				DrawIcon.drawFuncs.put(k,v);
			}{
				"Needs wslib Quark DrawIcon class.".error;

			}
		}

	}

	*getFuncs{
		^(
			\clone: { | rect|
				var square,rect2;
				var insetFactor = 0.55;
				var moveX;
				var center = rect.center;
				var rectSize=min(rect.width,rect.height)*insetFactor;
				var offset = rectSize.neg*0.2;
				var offset2 = offset.neg*0.5;

				square = Rect.aboutPoint(center,rectSize*0.5,rectSize*0.5);
				Pen.translate(offset2,offset2);
				Pen.addRect(square.moveBy(offset,offset) );

				Pen.stroke;

				Pen.moveTo(Point(square.right+offset,square.top));
				Pen.lineTo(Point(square.right,square.top));
				Pen.lineTo(Point(square.right,square.bottom));
				Pen.lineTo(Point(square.left,square.bottom));
				Pen.lineTo(Point(square.left,square.bottom+offset));

				Pen.stroke;
			},
			\scope: { | rect|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.1;
				Pen.width= drawRect.height*0.07;
				Pen.moveTo(Point(drawRect.left,drawRect.center.y));
				Pen.lineTo(Point(drawRect.left+(2*interval),drawRect.center.y));
				Pen.lineTo(Point(drawRect.left+(3*interval),drawRect.bottom));
				Pen.lineTo(Point(drawRect.left+(4*interval),drawRect.top));
				Pen.lineTo(Point(drawRect.left+(5*interval),drawRect.top+(drawRect.height*0.8)));
				Pen.lineTo(Point(drawRect.left+(6*interval),drawRect.top+(drawRect.height*0.2)));
				Pen.lineTo(Point(drawRect.left+(7*interval),drawRect.top+(drawRect.height*0.9)));
				Pen.lineTo(Point(drawRect.left+(8*interval),drawRect.center.y));
				Pen.lineTo(Point(drawRect.right,drawRect.center.y));

				Pen.stroke;
			},
			\fscope: { | rect|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.1;
				Pen.width= drawRect.height*0.07;
				Pen.moveTo(Point(drawRect.left,drawRect.bottom));
				Pen.lineTo(Point(drawRect.left+(1*interval),drawRect.bottom));
				Pen.lineTo(Point(drawRect.left+(2*interval),drawRect.bottom));
				Pen.lineTo(Point(drawRect.left+(3*interval),drawRect.top));
				Pen.lineTo(Point(drawRect.left+(4*interval),drawRect.top+(drawRect.height*0.9)));
				Pen.lineTo(Point(drawRect.left+(5*interval),drawRect.top+(drawRect.height*0.3)));
				Pen.lineTo(Point(drawRect.left+(6*interval),drawRect.top+(drawRect.height)));
				Pen.lineTo(Point(drawRect.left+(7*interval),drawRect.top+(drawRect.height*0.3)));
				Pen.lineTo(Point(drawRect.left+(8*interval),drawRect.bottom));
				Pen.lineTo(Point(drawRect.right,drawRect.bottom));

				Pen.stroke;

			},

			\freq: { | rect|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.1;
				var amp = drawRect.height*0.35;
				Pen.width= drawRect.height*0.07;
				Pen.moveTo(Point(drawRect.left,drawRect.center.y));
				Pen.lineTo(Point(drawRect.left+(2*interval),drawRect.center.y));
				Pen.lineTo(Point(drawRect.left+(3*interval),drawRect.center.y-(amp*0.5)));
				Pen.lineTo(Point(drawRect.left+(4*interval),drawRect.center.y+(amp)));
				Pen.lineTo(Point(drawRect.left+(5*interval),drawRect.center.y-(amp)));
				Pen.lineTo(Point(drawRect.left+(6*interval),drawRect.center.y+(amp)));
				Pen.lineTo(Point(drawRect.left+(7*interval),drawRect.center.y-(amp*0.5)));
				Pen.lineTo(Point(drawRect.left+(8*interval),drawRect.center.y));
				Pen.lineTo(Point(drawRect.right,drawRect.center.y));

				Pen.stroke;
			},


			\bug: { | rect|
				var rectSize=rect.width.min(rect.height);
				var inset=rectSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);

				var h = drawRect.height;
				var penwidth = h*0.1;

				var head=drawRect.height*0.225;
				var centerBottom=drawRect.center.y_(drawRect.bottom);
				var bodyR=drawRect.height/3.8;
				var bodyHw=drawRect.height/4.8;
				var hr=bodyHw*0.6;

				var bodyTop = drawRect.top+(drawRect.height*0.3);
				var leg=bodyR*0.7;
				var legGap=bodyR*0.6;
				var transl=bodyR*0.25;

				Pen.joinStyle=1;
				Pen.capStyle=1;
				Pen.width=penwidth;

				//body
				Pen.line(Point(drawRect.center.x-bodyR,bodyTop),Point(drawRect.center.x-bodyR,drawRect.bottom-bodyR));
				Pen.addArc(centerBottom-Point(0,bodyR),bodyR,pi,-pi );
				Pen.line(Point(drawRect.center.x+bodyR,drawRect.bottom-bodyR),Point(drawRect.center.x+bodyR,bodyTop));
				Pen.line(Point(drawRect.center.x+bodyR,bodyTop),Point(drawRect.center.x-bodyR,bodyTop));
				Pen.line(Point(drawRect.center.x,bodyTop),Point(drawRect.center.x,drawRect.bottom-bodyR));
				Pen.stroke;

				//head
				Pen.moveTo(Point(drawRect.center.x-bodyHw,bodyTop));
				Pen.arcTo(Point(drawRect.center.x-bodyHw,bodyTop-bodyHw),Point(drawRect.center.x,bodyTop-bodyHw),hr);

				Pen.moveTo(Point(drawRect.center.x+bodyHw,bodyTop));
				Pen.arcTo(Point(drawRect.center.x+bodyHw,bodyTop-bodyHw),Point(drawRect.center.x,bodyTop-bodyHw),hr);
				Pen.line(Point(drawRect.center.x-bodyHw+hr,bodyTop-bodyHw),Point(drawRect.center.x+bodyHw-hr,bodyTop-bodyHw));

				Pen.line(Point(drawRect.center.x-bodyHw+hr,bodyTop-bodyHw),Point(drawRect.center.x-bodyHw+(hr/2),drawRect.top));
				Pen.line(Point(drawRect.center.x+bodyHw-hr,bodyTop-bodyHw),Point(drawRect.center.x+bodyHw-(hr/2),drawRect.top));
				Pen.stroke;
				Pen.translate(0,transl.neg);

				// legs
				Pen.moveTo(Point(drawRect.center.x-bodyR,drawRect.center.y));
				Pen.arcTo(Point(drawRect.center.x-bodyR-leg,drawRect.center.y),Point(drawRect.center.x-bodyR-leg,drawRect.center.y-leg),leg);

				Pen.moveTo(Point(drawRect.center.x+bodyR,drawRect.center.y));
				Pen.arcTo(Point(drawRect.center.x+bodyR+leg,drawRect.center.y),Point(drawRect.center.x+bodyR+leg,drawRect.center.y-leg),leg);

				Pen.moveTo(Point(drawRect.center.x-bodyR,drawRect.center.y+legGap+legGap));
				Pen.arcTo(Point(drawRect.center.x-bodyR-leg,drawRect.center.y+legGap+legGap),Point(drawRect.center.x-bodyR-leg,drawRect.center.y+legGap+legGap+leg),leg);

				Pen.moveTo(Point(drawRect.center.x+bodyR,drawRect.center.y+legGap+legGap));
				Pen.arcTo(Point(drawRect.center.x+bodyR+leg,drawRect.center.y+legGap+legGap),Point(drawRect.center.x+bodyR+leg,drawRect.center.y+legGap+legGap+leg),leg);

				Pen.line(Point(drawRect.left,drawRect.center.y+legGap),Point(drawRect.center.x-bodyR,drawRect.center.y+legGap));
				Pen.line(Point(drawRect.right,drawRect.center.y+legGap),Point(drawRect.center.x+bodyR,drawRect.center.y+legGap));



				Pen.stroke;
			},

			\archive: { | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.2;
				var insetH=rectSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-insetH,(rectSize * 0.5)-inset).moveBy(0,inset/5);
				var w = drawRect.height;
				var penwidth = w*0.1;
				var crn=drawRect.height*0.08;
				var crn2=drawRect.height*0.2;
				var segm = drawRect.width-(2*crn);
				var inset1=(drawRect.width*0.33);
				var inset2=(drawRect.width*0.25);
				var inset3=(drawRect.width*0.07);
				var insetHeight=(drawRect.height*0.25);
				var lid=drawRect.height*0.225;
				var handle = lid;
				var radius = drawRect.height*0.05;

				Pen.width=penwidth;
				Pen.moveTo(Point(drawRect.right-inset3,drawRect.top+lid+penwidth));
				Pen.arcTo(Point(drawRect.right-inset3,drawRect.bottom),Point(drawRect.right-inset3-crn,drawRect.bottom),crn);
				Pen.lineTo(Point(drawRect.left+crn+inset3,drawRect.bottom));
				Pen.arcTo(Point(drawRect.left+inset3,drawRect.bottom),Point(drawRect.left,drawRect.top),crn);
				Pen.lineTo(Point(drawRect.left+inset3,drawRect.top+lid+penwidth));
				Pen.stroke;
				Pen.moveTo(drawRect.left,drawRect.top);
				Pen.addRoundedRect(Rect(drawRect.left,drawRect.top,drawRect.width,lid+penwidth),radius,radius);

				Pen.line(drawRect.center-Point(handle,penwidth.neg),drawRect.center+Point(handle,penwidth));

				Pen.stroke;
			},

			\lightdark: { | rect|
				var  m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.1;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				m=drawRect.width;
				n=inset;
				Pen.translate(drawRect.left-(inset*0.5),drawRect.top-(inset*0.5));
				// Pen.color_(Color.grey);
				Pen.moveTo(n@n);
				Pen.lineTo(m@n);
				Pen.lineTo(m@m);
				Pen.stroke;
				Pen.moveTo(m@m);
				Pen.lineTo(n@m);
				Pen.lineTo(n@n);
				Pen.fill;
			},


			\save: { | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var w = drawRect.height;
				var penwidth = w*0.1;
				var crn=drawRect.height*0.08;
				var crn2=drawRect.height*0.2;
				var segm = drawRect.width-(2*crn);
				var inset1=(drawRect.width*0.33);
				var inset2=(drawRect.width*0.25);
				var insetHeight=(drawRect.height*0.25);

				Pen.width=penwidth;
				Pen.moveTo(Point(drawRect.left+crn,drawRect.top));
				Pen.lineTo(Point(drawRect.right-crn2,drawRect.top));
				Pen.lineTo(Point(drawRect.right,drawRect.top+crn2));
				Pen.arcTo(Point(drawRect.right,drawRect.bottom),Point(drawRect.right-crn,drawRect.bottom),crn);
				Pen.lineTo(Point(drawRect.left+crn,drawRect.bottom));
				Pen.arcTo(Point(drawRect.left,drawRect.bottom),Point(drawRect.left,drawRect.top),crn);
				Pen.lineTo(Point(drawRect.left,drawRect.top+crn));
				Pen.arcTo(Point(drawRect.left,drawRect.top),Point(drawRect.left+crn,drawRect.top),crn);

				Pen.moveTo(Point(drawRect.left+inset1,drawRect.top));
				Pen.lineTo(Point(drawRect.left+inset1,drawRect.top+insetHeight-crn));
				Pen.arcTo(Point(drawRect.left+inset1,drawRect.top+insetHeight),Point(drawRect.left+inset1+crn,drawRect.top+insetHeight),crn);
				Pen.lineTo(Point(drawRect.right-inset1-crn,drawRect.top+insetHeight));
				Pen.arcTo(Point(drawRect.right-inset1,drawRect.top+insetHeight),Point(drawRect.right-inset1,drawRect.top+insetHeight-crn),crn);
				Pen.lineTo(Point(drawRect.right-inset1,drawRect.top));

				Pen.moveTo(Point(drawRect.left+inset2,drawRect.bottom));
				Pen.lineTo(Point(drawRect.left+inset2,drawRect.bottom-insetHeight));
				Pen.arcTo(Point(drawRect.left+inset2,drawRect.bottom-insetHeight-crn),Point(drawRect.left+inset2+crn,drawRect.bottom-insetHeight-crn),crn);
				Pen.lineTo(Point(drawRect.right-inset2-crn,drawRect.bottom-insetHeight-crn));
				Pen.arcTo(Point(drawRect.right-inset2,drawRect.bottom-insetHeight-crn),Point(drawRect.right-inset2,drawRect.bottom-insetHeight+crn),crn);
				Pen.lineTo(Point(drawRect.right-inset2,drawRect.bottom));

				//Pen.lineTo(Point(drawRect.left-crn, drawRect.bottom));


				Pen.stroke;


			},

			\microphone:{ |rect|
				var inset = 1;
				var rectSize=min(rect.width,rect.height)*0.5;
				var rect2= Rect.aboutPoint(rect.center,rectSize,rectSize);
				var ir= rect2.insetBy(rect2.width*0.35,2);
				var scale= rect2.width/(rect2.width+(2*inset));
				var cx = ir.width*0.5;
				var rad = ir.width*0.9;
				var matrix = [scale, 0, 0, scale, inset*scale, inset*scale];
				var cy = rect2.left+rad;

				Pen.matrix_(matrix);

				Pen.width_(rect2.width*0.08);
				Pen.addRoundedRect(ir.height_(ir.height*0.7),ir.width*0.5,ir.width*0.5);
				Pen.fill;
				Pen.addArc(Point(rect2.center.x,rect2.height*0.55), rad, 2*pi, pi);
				Pen.line(Point(rect2.center.x,(rect2.height*0.55+rad)),Point(rect2.center.x,rect2.height));
				Pen.stroke;
			},

			\midi:{ | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.13;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var rad = drawRect.height*0.5;
				var x = drawRect.center.x;
				var y = drawRect.center.y;

				Pen.width = 1.5.max(rad*0.18);
				5.do{

					Pen.addArc(Point(drawRect.center.x-(rad*0.55),drawRect.center.y), rad*0.13, pi, 2*pi);

					Pen.fill;
					Pen.rotate(0.25*pi,x,y);
				};
				Pen.rotate(3*0.25*pi,x,y);


				Pen.addArc(Point(drawRect.center.x,drawRect.center.y+(rad*0.90)), rad*0.2, pi-0.24, pi+0.48);
				Pen.fillStroke;


				Pen.moveTo(drawRect.center);
				Pen.addArc(drawRect.center, rad, pi, 2*pi);

				Pen.stroke;


			},


			\eye: { | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.1;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var rad = drawRect.height*0.5;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var angleDiff = 0.42;
				// Pen.line(Point(rect.left,rect.height*0.5),Point(rect.right,rect.height*0.5));
				// Pen.stroke;
				Pen.width= (rad*0.06).ceil(2);
				Pen.addArc(drawRect.center, rad*0.3, pi, 2*pi);

				Pen.fillStroke;

				Pen.moveTo(drawRect.center);


				Pen.addArc(Point(drawRect.center.x, drawRect.center.y+(rad*0.5)), rad*1.2, pi+angleDiff,pi-(angleDiff*2));
				Pen.addArc(Point(drawRect.center.x, drawRect.center.y-(rad*0.5)), rad*1.2, 2pi+angleDiff,pi-(angleDiff*2));

				Pen.stroke;



			},

			\eyeFill:{ | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.1;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var rad = drawRect.height*0.5;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var angleDiff = 0.42;
				// Pen.line(Point(rect.left,rect.height*0.5),Point(rect.right,rect.height*0.5));
				// Pen.stroke;
				Pen.width= (rad*0.06).ceil(2);


				Pen.moveTo(drawRect.center);


				Pen.addArc(Point(drawRect.center.x, drawRect.center.y+(rad*0.5)), rad*1.2, pi+angleDiff,pi-(angleDiff*2));
				Pen.addArc(Point(drawRect.center.x, drawRect.center.y-(rad*0.5)), rad*1.2, 2pi+angleDiff,pi-(angleDiff*2));
				Pen.fill;
				Pen.strokeColor_(Color.black);
				Pen.stroke;
				Pen.addArc(drawRect.center, rad*0.3, pi, 2*pi);
				Pen.color_(Color.grey(0.8));
				Pen.strokeColor_(Color.grey(0.8));

				Pen.fillStroke;

			},



			\list: { | rect|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var w = drawRect.height;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var penwidth = 2.max(w*0.08);
				var space = w*0.08;
				Pen.width=penwidth;

				Pen.line(Point(drawRect.left,y*0.6),Point(drawRect.left+(2*space),y*0.6));
				Pen.line(Point(drawRect.left+(4*space),y*0.6),Point(drawRect.right,y*0.6));

				Pen.line(Point(drawRect.left,y),Point(drawRect.left+(2*space),y));
				Pen.line(Point(drawRect.left+(4*space),y),Point(drawRect.right,y));

				Pen.line(Point(drawRect.left,y*1.4),Point(drawRect.left+(2*space),y*1.4));
				Pen.line(Point(drawRect.left+(4*space),y*1.4),Point(drawRect.right,y*1.4));

				Pen.stroke;



			},

			\speaker2: { | rect|
				var square;
				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 3,
					rect.width.min( rect.height ) / 3 );

				square = square.insetBy( square.width / 6, 0 );
				square = square.moveBy( square.width / -3, 0 );

				Pen.moveTo( square.rightTop );
				Pen.lineTo( square.rightBottom );
				Pen.lineTo( (square.left + (square.width / 2.5))@
					(square.center.y + (square.width / 4)) );
				Pen.lineTo( square.left@(square.center.y + (square.width / 4)) );
				Pen.lineTo( square.left@(square.center.y - (square.width / 4)) );
				Pen.lineTo( (square.left + (square.width / 2.5))@
					(square.center.y - (square.width / 4)) );
				Pen.lineTo( square.rightTop );
				Pen.translate(square.width.neg*0.2,0);
				Pen.draw;
				Pen.width=1.5;
				Pen.addArc((square.center.x +(square.width/3))@square.center.y,square.width*0.4 ,-0.15*pi,pi*0.3 );
				Pen.addArc((square.center.x +(square.width/3))@square.center.y,square.width*0.8 ,-0.15*pi,pi*0.3 );
				Pen.addArc((square.center.x +(square.width/3))@square.center.y,square.width*1.2 ,-0.15*pi,pi*0.3 );
				Pen.stroke;

			},

			\sine2: { |rect, n = 1, phase = 0, res, fitToRect = false|

				var square, wd, step;

				if( fitToRect )
				{ square = Rect.aboutPoint( rect.center,
					rect.width / 2.5,
					rect.height / 4 ); }
				{ square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 3,
					rect.width.min( rect.height ) / 4 ); };


				wd = square.height.min( square.width ) / 5;

				res = res ?? { (square.width / 6).ceil.max( 50 ) };


				step = (square.width / (n*res));

				Pen.width = wd;

				((n*res) + 1).do({ |i|
					var point;
					point = ((i*step) + square.left)
					@(((( (i / res) * 2pi ) + phase).sin.neg + 1 * (square.height / 2))
						+ square.top);
					if( i == 0 )
					{ Pen.moveTo( point ) }
					{ Pen.lineTo( point ) };
				});
				Pen.stroke;

			},

			\pulse:{ | rect|
				var m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var segx= drawRect.height*0.35;
				var segy= drawRect.height*0.4;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				m=drawRect.width;
				n=inset;
				Pen.joinStyle=0;
				Pen.moveTo(drawRect.center);
				Pen.lineTo(Point(x,y-segy));
				Pen.lineTo(Point(x+segx,y-segy));
				Pen.lineTo(Point(x+segx,y+segy));
				Pen.lineTo(Point(x+(segx*1.7),y+segy));
				Pen.moveTo(drawRect.center);
				Pen.lineTo(Point(x,y+segy));
				Pen.lineTo(Point(x-segx,y+segy));
				Pen.lineTo(Point(x-segx,y-segy));
				Pen.lineTo(Point(x-(segx*1.7),y-segy));

				Pen.stroke;
			},
			\pulse2:{ | rect|
				var m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.2;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var segx= drawRect.height*0.35;
				var segy= drawRect.height*0.4;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				m=drawRect.width;
				n=inset;
				Pen.joinStyle=0;
				Pen.width = m*0.15;
				Pen.moveTo(drawRect.center);
				Pen.lineTo(Point(x,y-segy));
				Pen.lineTo(Point(x+segx,y-segy));
				Pen.lineTo(Point(x+segx,y+segy));
				Pen.lineTo(Point(x+(segx*1.7),y+segy));
				Pen.moveTo(drawRect.center);
				Pen.lineTo(Point(x,y+segy));
				Pen.lineTo(Point(x-segx,y+segy));
				Pen.lineTo(Point(x-segx,y-segy));
				Pen.lineTo(Point(x-(segx*1.7),y-segy));

				Pen.stroke;
			}
		);


	}

	*listKeys{
		this.getFuncs.order.do{|k|
			("\\" ++ k.asString).postln;
		}
	}
}