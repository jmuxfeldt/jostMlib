JMDrawIcon {
	classvar lightColor, randomSeed=77, globalInsetFactor=0.1;
	*new { |name, rect ... more|
		rect = (rect ? (32@32)).asRect;
		^this.drawFuncs.at(name).value( rect, *more ); // returns pen function
	}



	*initClass{
		\DrawIcon.asClass.notNil.if{
			Class.initClassTree(DrawIcon);
			this.drawFuncs.keysValuesDo{|k,v|
				DrawIcon.drawFuncs.put(k,v);
			}
		};
		lightColor = QtGUI.palette.window;

	}

	*symbolArgs { |name, rect|
		var more;
		more = name.asString.split( $_ );
		name = more[0].asSymbol;
		more = more[1..].collect( _.interpret );
		^this.new( name, rect, *more );
	}

	*lightColor_{|color| lightColor= color}


	*drawFuncs{
		^(
			\clone: { | rect, insetFactor|
				var square,rect2;
				var scale = 0.55;
				var moveX;
				var center = rect.center;
				var offset;
				var offset2;
				var penWidth ;
				var rectSize=(min(rect.width,rect.height)*scale);
				rectSize = rectSize-(rectSize*(insetFactor??globalInsetFactor));
				offset = rectSize.neg*0.2;
				offset2 = offset.neg*0.5;
				penWidth = 1.max(rectSize*0.05);
				Pen.width=penWidth;

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

			\sample:	{ | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.1);
				var drawRect=Rect.aboutPoint(rect.center,(rect.width * 0.5)-inset,(iconSize * 0.5)-inset);
				var size=drawRect.height;
				var penWidth= (size*0.07);
				var interval= (penWidth*2.2);
				var x=drawRect.left;
				var y=rect.center.y;
				var n =(drawRect.width/(interval))+1;
				var nY =(drawRect.height*0.6);
				var slots = Pseed(randomSeed+12, Pmeanrand(2, nY, n)).asStream.nextN(n);
				Pen.width= penWidth;
				nY.do{|k|
					x=drawRect.left;
					n.do{|i|
						Pen.line(Point(x,y-slots.at(i)),Point(x,y+slots.at(i)));
						x=x+interval;y;
					};
				};

				Pen.stroke;

			},
			\noise:	{ | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.1);
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,(iconSize * 0.5)-inset);
				var size=drawRect.height;
				var penWidth= 1;
				var interval= (penWidth);
				var x=drawRect.left;
				var y=rect.center.y;
				var n =(drawRect.width/(interval))+1;
				var nY =(drawRect.height*0.6);
				var slots = Pseed(randomSeed+119, Pwhite(0, nY, n)).asStream.nextN(n);
				Pen.width= penWidth;
				nY.do{|k|
					x=drawRect.left;
					n.do{|i|
						Pen.line(Point(x,y-slots.at(i)),Point(x,y+slots.at(i)));
						x=x+interval;y;
					};
				};

				Pen.stroke;

			},
			\levels:	{ | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.1);
				var drawRect=Rect.aboutPoint(rect.center,(rect.width * 0.5)-inset,(iconSize * 0.5)-inset);
				var size=drawRect.height;
				var penWidth= 2.max(iconSize*0.1);
				var interval= 2.5.max(penWidth*1.25);
				var x=drawRect.left;
				var y=drawRect.bottom;
				var n =(drawRect.width/(interval))+1;
				var nY =(drawRect.height/(interval));
				var slots = Pseed(randomSeed, Pwhite(1, nY, n)).asStream.nextN(n);
				Pen.width= penWidth;
				nY.do{|k|
					x=drawRect.left;
					n.do{|i|
						slots.at(i).notNil.if{
							(slots.at(i)>k).if{
								Pen.line(Point(x,y),Point(x,y-penWidth));
							};
						};
						x=x+interval;y;
					};
					y=y-interval;
				};

				Pen.stroke;

			},

			\cam:	{ | rect, insetFactor|
				var square, square2;
				var penwidth ;
				var xwidth;
				insetFactor=(insetFactor??globalInsetFactor)-0.05;
				square = Rect.aboutPoint( rect.center,
					1.35*rect.width.min( rect.height ) / 3,
					1.35*rect.width.min( rect.height ) / 5.5 );
				square =square.insetBy(square.width*insetFactor,square.height*insetFactor);
				penwidth = 1.max(square.width*0.075);
				xwidth=square.width*0.25;
				square2=square.copy.width_(square.width*0.67);

				Pen.addRoundedRect(square2, penwidth,penwidth);

				Pen.moveTo( square.rightTop );
				Pen.lineTo( square.rightBottom );
				Pen.lineTo( (square.left + (square.width / 1.4))@
					(square.center.y + (square.height / 4)) );
				Pen.lineTo( (square.left + (square.width /  1.4))@
					(square.center.y - (square.height / 4)) );
				Pen.lineTo( square.rightTop );
				Pen.fill;


			},

			\meter:	{ | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*(insetFactor??globalInsetFactor)*0.5;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var mCenter = rect.center.y_(rect.center.y+(rectSize*0.4)-inset);
				var meterSize = drawRect.width;
				var penWidth = 1.max(1.5*meterSize*0.035);
				var penWidth2 = 1.max(meterSize*0.035);
				var penWidth3 = 1.max(meterSize*0.035);
				Pen.use {
					Pen.width=penWidth;

					// Pen.addOval(drawRect);
					// Pen.stroke;
					Pen.width=penWidth2;

					Pen.rotate(0.3pi,mCenter.x,mCenter.y);
					7.do{
						Pen.line(Point(mCenter.x-(meterSize*0.7),mCenter.y), Point(mCenter.x-(meterSize*0.55), mCenter.y));
						Pen.stroke;
						Pen.rotate(0.066pi,mCenter.x,mCenter.y);
					};
					Pen.stroke;
					Pen.rotate(0.37pi.neg,mCenter.x,mCenter.y);
					Pen.line(Point(mCenter.x-(meterSize*0.8),mCenter.y), Point(mCenter.x, mCenter.y));
					Pen.width=penWidth2;
					Pen.stroke;
				};
				Pen.width=penWidth2;

				Pen.addWedge(mCenter,drawRect.width*0.25,0,pi.neg);
				Pen.fill;

			},
			\dvd:	{ | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);

				var inset=rectSize*(insetFactor??globalInsetFactor);
				var square=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var size=square.width;
				var penWidth = 1.max(size*0.045);
				Pen.width_(penWidth);
				Pen.addArc(rect.center,(size/6)-(1.5*penWidth), 0 ,2*pi);
				Pen.fill;

				Pen.addArc(rect.center,size/2, 0 ,2*pi);
				Pen.addArc(rect.center,size/6, 0 ,2*pi);
				Pen.stroke;

				Pen.addAnnularWedge(rect.center,size/6,(size*0.5)-(penWidth),  1+0.4pi, 0.1pi);
				Pen.addAnnularWedge(rect.center,size/6,(size*0.5)-(penWidth), 1+pi+0.4pi, 0.1pi);
				Pen.stringAtPoint("DVD",rect.center+((size*0.11).neg@(size*0.2)),Font.default.size_(size/5.5).bold_(true));
				Pen.fill;


			},
			\cd:	{ | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);

				var inset=rectSize*(insetFactor??globalInsetFactor);
				var square=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var size=square.width;
				var penWidth = 1.max(size*0.045);
				Pen.width_(penWidth);
				Pen.addArc(rect.center,(size/6)-(1.5*penWidth), 0 ,2*pi);
				Pen.fill;

				Pen.addArc(rect.center,size/2, 0 ,2*pi);
				Pen.addArc(rect.center,size/6, 0 ,2*pi);
				Pen.stroke;

				Pen.addAnnularWedge(rect.center,size/6,(size*0.5)-(penWidth),  1+0.4pi, 0.1pi);
				Pen.addAnnularWedge(rect.center,size/6,(size*0.5)-(penWidth), 1+pi+0.4pi, 0.1pi);
				Pen.fill;


			},
			\scope: { | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*(insetFactor??globalInsetFactor);
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
			\fscope: { | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.07);
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



			\freq: { | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*(insetFactor??globalInsetFactor);
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

			\del: { | rect, insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*(insetFactor??globalInsetFactor);
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,(iconSize * 0.5)-inset);
				var interval = drawRect.width/3;
				var intervalV = drawRect.height/8;
				var rad =drawRect.width*0.16;
				var xSq=drawRect.insetBy(interval).moveBy(interval*0.33,0);
				Pen.joinStyle=1;
				Pen.capStyle=1;

				Pen.width = drawRect.width * 0.08;
				Pen.line(Point(drawRect.left,drawRect.center.y),Point(drawRect.left+interval,drawRect.top+intervalV));
				Pen.arcTo(Point(drawRect.right,drawRect.top+intervalV),Point(drawRect.right,drawRect.center.y),rad);
				Pen.arcTo(Point(drawRect.right,drawRect.bottom-intervalV),Point(drawRect.left+interval,drawRect.bottom-intervalV),rad);
				Pen.lineTo(Point(drawRect.left+interval,drawRect.bottom-intervalV));
				Pen.line(Point(drawRect.left,drawRect.center.y),Point(drawRect.left+interval,drawRect.bottom-intervalV));
				Pen.stroke;
				Pen.line(
					Polar( xSq.width / 2, 1.25pi ).asPoint + xSq.center,
					Polar( xSq.width / 2, 0.25pi ).asPoint + xSq.center
				);
				Pen.line(
					Polar( xSq.width / 2, 1.75pi ).asPoint + xSq.center,
					Polar( xSq.width / 2, 0.75pi ).asPoint + xSq.center
				);

				Pen.stroke;
			},

			\bug: { | rect, insetFactor|
				var rectSize=rect.width.min(rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)+0.05);
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

			\archive: { | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.15;
				var insetH=inset*0.75;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-insetH,(rectSize * 0.5)-inset)
				.moveBy(0,inset/5).insetBy(rectSize*(insetFactor??globalInsetFactor)*0.5,rectSize*(insetFactor??globalInsetFactor)*0.5);
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

			\lightdark: { | rect, insetFactor|
				var  m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*(((insetFactor??globalInsetFactor)*0.5)+0.05);
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


			\lightdark2: { | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)*0.5);
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.45)-inset,(rectSize * 0.5)-inset);
				Pen.width=0;
				Pen.rotate(0.25pi, drawRect.center.x,drawRect.center.y);
				Pen.addWedge(rect.center,drawRect.width*0.5,0,pi);
				Pen.fillStroke;
				Pen.width=1;
				Pen.addArc(rect.center,drawRect.width*0.5,pi,2pi);
				Pen.stroke;
			},

			\save: { | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset)
				.insetBy(rectSize*(insetFactor??globalInsetFactor)*0.5,rectSize*(insetFactor??globalInsetFactor)*0.5);
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

			\microphone:{ |rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset = rectSize*(insetFactor??globalInsetFactor);
				var drawRect= Rect.aboutPoint(rect.center,(rectSize*0.55)-inset,(rectSize*0.55)-inset);
				var ir= drawRect.insetBy(drawRect.width*0.35,2);
				var scale= drawRect.width/(drawRect.width+(2));
				var cx = ir.width*0.5;
				var rad = ir.width*0.9;
				var matrix = [scale, 0, 0, scale, scale, scale];
				var cy = drawRect.left+rad;
				var stand1Y=drawRect.center.y+(rectSize*0.07);
				var stand2Y=stand1Y+rad;

				Pen.matrix_(matrix);

				Pen.width_(drawRect.width*0.08);
				Pen.addRoundedRect(ir.height_(ir.height*0.7),ir.width*0.5,ir.width*0.5);
				Pen.fill;
				Pen.addArc(Point(drawRect.center.x,stand1Y), rad, 2*pi, pi);
				Pen.line(Point(drawRect.center.x,stand2Y),Point(drawRect.center.x,stand2Y+(rad*0.7)));
				Pen.stroke;

			},

			\midi:{ | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)+0.03);
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


			\eye: { | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)-0.01);
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var rad = drawRect.height*0.42;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var angleDiff = 0.55;
				// Pen.line(Point(rect.left,rect.height*0.5),Point(rect.right,rect.height*0.5));
				// Pen.stroke;
				Pen.width= (rad*0.06).ceil(2);

				Pen.addArc(Point(drawRect.center.x, drawRect.center.y+(rad*0.80)), rad*1.5, pi+angleDiff,pi-(angleDiff*2));
				Pen.addArc(Point(drawRect.center.x, drawRect.center.y-(rad*0.80)), rad*1.5, 2pi+angleDiff,pi-(angleDiff*2));
				Pen.stroke;

				Pen.moveTo(drawRect.center);
				Pen.addArc(drawRect.center, rad*0.3, pi, 2*pi);

				Pen.fillStroke;



			},

			\eyeFill:{ | rect,insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)-0.01);
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var rad = drawRect.height*0.42;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var angleDiff = 0.55;
				// Pen.line(Point(rect.left,rect.height*0.5),Point(rect.right,rect.height*0.5));
				// Pen.stroke;
				Pen.width= (rad*0.06).ceil(2);

				Pen.moveTo(drawRect.center);

				Pen.addArc(Point(drawRect.center.x, drawRect.center.y+(rad*0.80)), rad*1.5, pi+angleDiff,pi-(angleDiff*2));
				Pen.addArc(Point(drawRect.center.x, drawRect.center.y-(rad*0.80)), rad*1.5, 2pi+angleDiff,pi-(angleDiff*2));
				Pen.fillStroke;
				Pen.color_(lightColor);
				Pen.strokeColor_(lightColor);

				Pen.addArc(drawRect.center, rad*0.25, pi, 2*pi);

				Pen.fillStroke;

			},



			\list: { | rect, insetFactor|
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)+0.05);
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var w = drawRect.height;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				var penwidth = 2.max(w*0.08);
				var space = w*0.08;
				Pen.width=penwidth;

				Pen.line(Point(drawRect.left,y+(w*0.35)),Point(drawRect.left+(2*space),y+(w*0.35)));
				Pen.line(Point(drawRect.left+(4*space),y+(w*0.35)),Point(drawRect.right,y+(w*0.35)));

				Pen.line(Point(drawRect.left,y),Point(drawRect.left+(2*space),y));
				Pen.line(Point(drawRect.left+(4*space),y),Point(drawRect.right,y));

				Pen.line(Point(drawRect.left,y-(w*0.35)),Point(drawRect.left+(2*space),y-(w*0.35)));
				Pen.line(Point(drawRect.left+(4*space),y-(w*0.35)),Point(drawRect.right,y-(w*0.35)));

				Pen.stroke;



			},

			\speaker2: { | rect,insetFactor|
				var square;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*(insetFactor??globalInsetFactor)*0.5;

				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 2.65,
					rect.width.min( rect.height ) / 2.65 );
				square=square.insetBy(inset,inset);
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
			\speaker3: { | rect,insetFactor|

				var square;
				var penwidth ;
				var size;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*(insetFactor??globalInsetFactor)*0.5;

				size = rect.width.min( rect.height )*0.5;
				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 2.65,
					rect.width.min( rect.height ) / 2.65 );
				square=square.insetBy(inset,inset);

				penwidth = 1.max(square.width*0.03);
				size = rect.width.min( rect.height )*0.37;

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
				// Pen.translate(square.width.neg*0.2,0);
				Pen.translate(square.width*0.08.neg,0);
				Pen.draw;
				Pen.width=1.5;
				Pen.addArc((square.center.x +(square.width/3))@square.center.y,square.width*0.8 ,-0.15*pi,pi*0.3 );
				Pen.addArc((square.center.x +(square.width/3))@square.center.y,square.width*1.2 ,-0.15*pi,pi*0.3 );
				Pen.translate(square.width.neg*0.3,0);
				Pen.stroke;

			},
			\speakerMute: { | rect,insetFactor|
				var square;
				var penwidth ;
				var xwidth;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*(insetFactor??globalInsetFactor)*0.5;

				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 2.65,
					rect.width.min( rect.height ) / 2.65 );
				square=square.insetBy(inset,inset);
				penwidth = 1.max(square.width*0.075);


				xwidth=square.width*0.25;

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
				Pen.translate(square.width*0.08.neg,0);
				Pen.draw;
				//
				Pen.width_(penwidth*1.5);
				Pen.rotate(pi*0.25,rect.center.x,rect.center.y);
				Pen.line(Point(rect.center.x-xwidth,rect.center.y),Point(rect.center.x+xwidth,rect.center.y));
				Pen.line(Point(rect.center.x,rect.center.y-xwidth),Point(rect.center.x,rect.center.y+xwidth));
				Pen.translate(square.width*0.5,square.width*0.5.neg);

				Pen.stroke;

			},
			\reverb: { | rect,insetFactor|
				var rectSize=min(rect.width,rect.height);

				var inset=rectSize*((insetFactor??globalInsetFactor)+0.1);
				var square=Rect.aboutPoint(rect.center,(rectSize * 0.6)-inset,(rectSize * 0.5)-inset);

				Pen.width = 1.5.max(square.width*0.05);
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*0.4*0.47 , -0.15*pi,pi*0.3 );
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*0.8*0.47  , -0.15*pi,pi*0.3 );
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*1.2*0.47  , -0.15*pi,pi*0.3 );

				Pen.stroke;
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*0.4*0.47 , -0.15*pi,pi*0.3 );
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*0.8*0.47  , -0.15*pi,pi*0.3 );
				Pen.addArc((square.left-(square.width*0.4*0.4))@(square.top+(square.height*0.3)), square.width*1.2*0.47  , -0.15*pi,pi*0.3 );
				Pen.rotate(pi,rect.center.x, rect.center.y);

				Pen.stroke;

			},
			\sine2: { |rect,insetFactor|

				var square, wd, step,res, phase = 0,n = 1;
				var rectSize=min(rect.width,rect.height);

				var inset=rectSize*((insetFactor??globalInsetFactor)-0.1);

				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 3,
					rect.width.min( rect.height ) / 4 );
				square=square.insetBy(inset,inset);

				wd = square.height.min( square.width ) / 8;

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

			\pulse:{ | rect,insetFactor|
				var m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)+0.1);
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
			\pulse2:{ | rect,insetFactor|
				var m, n;
				var rectSize=min(rect.width,rect.height);
				var inset=rectSize*((insetFactor??globalInsetFactor)+0.1);
				var drawRect=Rect.aboutPoint(rect.center,(rectSize * 0.5)-inset,(rectSize * 0.5)-inset);
				var segx= drawRect.height*0.35;
				var segy= drawRect.height*0.4;
				var x = drawRect.center.x;
				var y = drawRect.center.y;
				m=drawRect.width;
				n=inset;
				Pen.joinStyle=0;
				Pen.width = m*0.1;
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

			\tri:{ | rect,insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.1);
				var inset2=iconSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,
					(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.2;
				var y=drawRect.top;
				Pen.addRect(drawRect);
				Pen.clip;
				Pen.width=  (drawRect.width*0.08).max(1);
				Pen.moveTo(Point(drawRect.left-interval,drawRect.bottom));
				7.do{|i|
					var p = i%2;
					Pen.lineTo(Point(drawRect.left+((i)*interval)
						,drawRect.top+(inset2*0.5)+( p*(drawRect.height-inset2))));
				};

				Pen.stroke;
			},
			\saw:{ | rect,insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.05);
				var inset2=iconSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,
					(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.275;
				var y=drawRect.top;
				Pen.addRect(drawRect);
				Pen.clip;
				// Pen.width=  drawRect.width*0.08;
				Pen.moveTo(Point(drawRect.left,drawRect.bottom-(inset2*0.5)));
				7.do{|i|
					Pen.lineTo(Point(drawRect.left+(i*interval)
						,drawRect.bottom-(inset2*0.5)));
					Pen.lineTo(Point(drawRect.left+((i+1)*interval)
						,drawRect.top+(inset2*0.5)));

				};

				Pen.stroke;
			},
			\saw2:{  | rect,insetFactor|
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)+0.05);
				var inset2=iconSize*0.15;
				var drawRect=Rect.aboutPoint(rect.center,(iconSize * 0.5)-inset,
					(iconSize * 0.5)-inset);
				var interval= drawRect.width*0.275;
				var y=drawRect.top;
				Pen.addRect(drawRect);
				Pen.clip;
				Pen.width=  drawRect.width*0.08;
				Pen.moveTo(Point(drawRect.left,drawRect.bottom-(inset2*0.5)));
				7.do{|i|
					Pen.lineTo(Point(drawRect.left+(i*interval)
						,drawRect.bottom-(inset2*0.5)));
					Pen.lineTo(Point(drawRect.left+((i+1)*interval)
						,drawRect.top+(inset2*0.5)));

				};

				Pen.stroke;
			},


			\fitH:{  | rect,insetFactor|
				var square, wd;
				var iconSize=rect.width.min(rect.height);

				var inset=iconSize*((insetFactor??globalInsetFactor)*0.25);

				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 4,
					rect.width.min( rect.height ) / 4);
				square=square.insetBy(inset,inset);

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width/2,0);
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width,0);

				Pen.fill;

			},
			\fitV:{  | rect, insetFactor|
				var square, wd;
				var direction= 0.5pi;
				var iconSize=rect.width.min(rect.height);

				var inset=iconSize*((insetFactor??globalInsetFactor)*0.25);

				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 4,
					rect.width.min( rect.height ) / 4);
				square=square.insetBy(inset,inset);

				Pen.rotate( direction, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width/2,0);
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width,0);

				Pen.fill;

				Pen.rotate( direction.neg, square.center.x, square.center.y );
			},
			\xArrow:{  | rect, insetFactor|
				var square,square2, wd;
				var direction=0;
				var iconSize=rect.width.min(rect.height);

				var inset=iconSize*((insetFactor??globalInsetFactor)*0.5);
				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 4.5,
					rect.width.min( rect.height ) / 4.5 );
				square=square.insetBy(inset,inset);

				square2 = Rect.aboutPoint( rect.center,
					square.width.min( rect.height ) / 6,
					square.width.min( rect.height ) / 6 );
				Pen.rotate( direction, square.center.x, square.center.y );
				Pen.addRect(square2);
				Pen.fill;

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate((square.width*0.5)+(square.width*(1/8)),0);
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width+(square.width*(2/8)),0);

				Pen.fill;
				Pen.rotate( 0.5pi, square.center.x, square.center.y );
				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate((square.width*0.5)+(square.width*(1/8)),(square.width*0.5)+(square.width*(1/8)));
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width+(square.width*(2/8)),0);

				Pen.fill;
				Pen.rotate( 0.5pi, square.center.x, square.center.y );

			},

			\xArrow2:{  | rect, insetFactor|
				var square,square2, wd;
				var direction=0.25pi;
				var iconSize=rect.width.min(rect.height);
				var inset=iconSize*((insetFactor??globalInsetFactor)*0.5);
				square = Rect.aboutPoint( rect.center,
					rect.width.min( rect.height ) / 4.5,
					rect.width.min( rect.height ) / 4.5 );
				square=square.insetBy(inset,inset);

				square2 = Rect.aboutPoint( rect.center,
					square.width.min( rect.height ) / 6,
					square.width.min( rect.height ) / 6 );
				Pen.rotate( direction, square.center.x, square.center.y );
				Pen.addRect(square2);
				Pen.fill;

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate((square.width*0.5)+(square.width*(1/8)),0);
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width+(square.width*(2/8)),0);

				Pen.fill;
				Pen.rotate( 0.5pi, square.center.x, square.center.y );
				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate((square.width*0.5)+(square.width*(1/8)),(square.width*0.5)+(square.width*(1/8)));
				Pen.fill;

				Pen.rotate( pi, square.center.x, square.center.y );

				Pen.moveTo( (square.left)@(square.center.y) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (1/3) ) ) );
				Pen.lineTo( (square.center.x)@(square.top) );
				Pen.lineTo( (square.right)@(square.center.y) );
				Pen.lineTo( (square.center.x)@(square.bottom) );
				Pen.lineTo( (square.center.x)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.top + (square.height * (2/3) ) ) );
				Pen.lineTo( (square.left)@(square.center.y) );
				Pen.translate(square.width+(square.width*(2/8)),0);

				Pen.fill;
				Pen.rotate( 0.5pi, square.center.x, square.center.y );

			}

		);


	}

	*listKeys{
		this.drawFuncs.order.do{|k|
			("\\" ++ k.asString).postln;
		}
	}
}

