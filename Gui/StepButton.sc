StepButton :  SCViewHolder {
	classvar >defaultIncrColor,parent,bounds, >defaultDecrColor;
	var  drawRect, drawFunc,target,horz,  incrColor, decrColor,color1,
	color2,>modifier=524288, >step=1,>bigStep=5,>gap=0.5, >customAction,inset;

	*initClass{
		defaultIncrColor=Color.grey.alpha_(0.6);
		defaultDecrColor=Color.grey.alpha_(0.6);
	}

	*new { arg parent, bounds, target, incrColor,decrColor, horz=false;

		^super.new.init( parent, bounds, target, incrColor,decrColor, horz);
	}

	init {| argParent, argBounds, argTarget, argIncrColor,argDecrColor, argHorz|
		parent=argParent;
		bounds=argBounds.asRect;
		target=argTarget;
		horz=argHorz;
		inset=Point(0,0);
		incrColor=argIncrColor?defaultIncrColor;
		decrColor=argDecrColor?defaultDecrColor;
		color1=incrColor;
		color2=decrColor;
		parent=argHorz;
		this.view = UserView(argParent, bounds);
		view.mouseDownAction={ arg v, x, y, modifiers, buttonNumber, clickCount;
			this.mouseDown(x, y, modifiers, buttonNumber, clickCount)
		};
		view.mouseUpAction={ arg v, x, y, modifiers, buttonNumber, clickCount;
			this.mouseUp(x, y, modifiers, buttonNumber, clickCount)
		};
		drawRect = this.view.bounds.moveTo(0,0).insetBy(inset.x,inset.y);

		view.drawFunc= {arg uview; this.drawWidget(uview)};

	}

	drawWidget{|uview|
		var centerh,centerv;
		centerh = drawRect.left+drawRect.width/2;
		centerv = drawRect.top+drawRect.height/2;
		horz.if{
			Pen.fillColor_(color1);
			Pen.moveTo(drawRect.right@centerv);
			Pen.lineTo( (centerh+gap)@drawRect.bottom);
			Pen.lineTo( (centerh+gap)@drawRect.top);
			Pen.moveTo(drawRect.right@centerv);
			Pen.fill;
			Pen.fillColor_(color2);
			Pen.moveTo(drawRect.left@centerv);
			Pen.lineTo( (centerh-gap)@drawRect.bottom);
			Pen.lineTo( (centerh-gap)@drawRect.top);
			Pen.moveTo(drawRect.left@centerv);
			Pen.fill;

		}{

			Pen.fillColor_(color1);
			Pen.moveTo(centerh@drawRect.top);
			Pen.lineTo( drawRect.right@(centerh-gap));
			Pen.lineTo( drawRect.left@(centerh-gap));
			Pen.lineTo(centerh@drawRect.top);
			Pen.fill;
			Pen.fillColor_(color2);
			Pen.moveTo(centerh@drawRect.bottom);
			Pen.lineTo( drawRect.right@(centerh+gap));
			Pen.lineTo( drawRect.left@(centerh+gap));
			Pen.lineTo(centerh@drawRect.bottom);
			Pen.fill;

		};

	}


	mouseDown{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;
		(modifier==modifiers).if{newVal=bigStep}{newVal=step};

		horz.if{
			(x>(view.bounds.width*0.5)).if{
				color1=incrColor.copy.alpha_(0.2);
				this.prDoAction(newVal);

			}{
				color2=decrColor.copy.alpha_(0.2);
				this.prDoAction(newVal.neg);


			}
		}{
			(y<(view.bounds.height*0.5)).if{
				this.prDoAction(newVal);
				color1=incrColor.copy.alpha_(0.2);
			}{
				color2=decrColor.copy.alpha_(0.2);
				this.prDoAction(newVal.neg);

			}
		};
		this.view.refresh;
	}

	mouseUp{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;
		horz.if{
			(x>(view.bounds.width*0.5)).if{
				color1=incrColor;

			}{
				color2=decrColor;
			}
		}{
			(y<(view.bounds.height*0.5)).if{
				color1=incrColor;
			}{
				color2=decrColor;
			}
		};
		this.view.refresh;
	}

	prDoAction{|stp|
		customAction.notNil.if{
			customAction.value(stp);
		}{
			target.valueAction_(target.value+stp);
		};

	}


}
