CollapseView : SCViewHolder{
	var buttonBounds, button,<>offset=2,<view;

	*viewClass { ^CompositeView }

	*new { arg parent, bounds, buttonBounds;
		^super.new.init(parent,bounds,buttonBounds);
	}

	init{ arg argParent, bounds,argButtonBounds;
		view = FlowView(argParent, bounds);
		buttonBounds=argButtonBounds?Point(16,16);
		^this;
	}

	flow { arg func, bounds;
		view.flow({|w|
			button=CollapseButton(w, buttonBounds).offset_(offset);
			func.value(w);
		},bounds);
		view.resizeToFit;
	}
	button{
		button.isNil.if{"You must call .flow first or the button is nil.".warn};
		^button;
	}


}

CollapseButton : SCViewHolder {
	classvar >clickModifier=524288;

	var	<parent,<>value=0;
	var	autoRemoves, <>offset=2, pview,<collapseFunction,<button,
			<>storeBounds,>color, <>minHeight=25,>drawFunction;


	*viewClass {^ UserView}

	*new { arg parent, bounds;
		^super.new.init(parent, bounds);

	}

	init { arg argParent, argBounds;
		var  w,switch=0,parentView,triangle;

		color=Color.grey;
		parentView = argParent.asView;
		argBounds=argBounds ? (14@14);

		view = this.class.viewClass.new(parentView, argBounds.asRect);
		if(parentView.children[parentView.children.size-1] === view,{
			parentView.children[parentView.children.size-1] = this;
		},{
			Error("CollapseButton unexpected result : parent's last child is not my view").throw;
		});

		w=argParent;
		pview=w.parent;

		if (pview.parent.class==Window.implClass)
			{storeBounds=pview.parent.bounds.copy;}
			{storeBounds=pview.bounds.copy};

		triangle={ |rect, angle = 0, size = 1, width = 1, mode = \fill|
				var radius, center, backCenter;
				radius = (rect.width.min( rect.height ) / 4) * size;
				center = rect.center + Polar( radius * (2/9), angle );
				backCenter =  center + Polar( radius, angle + pi ).asPoint;
				Pen.moveTo( backCenter );
				Pen.lineTo( backCenter + Polar( radius * width, angle + 1.5pi ).asPoint );
				Pen.lineTo( center + Polar( radius, angle ).asPoint );
				Pen.lineTo( backCenter + Polar( radius * width, angle + 0.5pi ).asPoint );
				Pen.lineTo( backCenter );
				Pen.perform( \fill );

		};
		this.drawFunc={
			var bnds;
			bnds=this.bounds;
			Pen.use{
			Pen.color_(color);
			triangle.value(bnds,[0,pi/2].at(value));
			};
		};

		collapseFunction={ arg val,mod;
			this.value=val;
			mod=mod? 0;
			if (val==0){
				///set the visibility of the children
				w.children.do{ arg ch,i;
					if (i>=offset){

						try{ ch.asView.visible=false}
						};
				};

				if (pview.parent.class==Window.implClass){
					//storeBounds=pview.parent.bounds;
					pview.parent.bounds= pview.parent
						.bounds.top_(pview.parent.bounds.bottom-minHeight-5).height_(minHeight+5);
				}{
					//storeBounds=pview.bounds;
					pview.reflowAll.resizeToFit;
					pview.bounds=pview.bounds.height_(minHeight);
					{{pview.parent.reflowAll.resizeToFit}.defer;
					 {pview.parent.parent.reflowAll.resizeToFit}.defer}.fork;

				};

			}{
				w.children.do{ arg ch,i;
					if (i>=offset){try{ ch.asView.visible=true}};
				};

				if (pview.parent.class==Window.implClass){
					pview.parent.bounds= pview.parent.bounds
						.top_(pview.parent.bounds.bottom-storeBounds.height)
						.height_(storeBounds.height);
				}{
					pview.bounds=storeBounds;
					pview.reflowAll.resizeToFit;
					{{pview.parent.reflowAll.resizeToFit}.defer;
					 {pview.parent.parent.reflowAll.resizeToFit}.defer}.fork;
				};
			};

			if(mod==clickModifier){
				switch=0;
				if (pview.parent.class!=Window){ // not the Window version
					pview.parent.children.do{arg child;
						if(child.class==FlowView || (child.class==CompositeView.implClass)){
							if ((child.children[0].children[0]
							.class==UserView.implClass)){//find CollapseButtons
								if (val==1){ //for openning
									if((child.children[0].children[0]==this.view)){
										switch=1; //select all after this view
									};
									if (switch==1){ //if after this view
										child.children[0].children[0].action.value(1);
									};
								}{			//unconditionally close all views
									child.children[0].children[0].action.value(0);
								};
							};
						};
					};
				};
			};
			this.refresh;
		};

		this.keyDownAction={ arg view, char;
			if (char == $ ,  		{this.valueAction_((value-1).abs)});
			if (char == $\r, 		{this.valueAction_((value-1).abs)});
			if (char == $\n, 		{this.valueAction_((value-1).abs)});
			if (char == 3.asAscii, 	{this.valueAction_((value-1).abs)});
		};
		this.mouseUpAction_( {arg button, x, y, mod;
 			this.valueAction_((value-1).abs,mod);
 			});
 		this.view.action={arg val;
 			this.valueAction_(val);
 		};

		this.valueAction_(1,0);
 	}

	valueAction_{ arg val,mod;
		this.collapseFunction.value(val.booleanValue.asInteger,mod);
	}

}


 