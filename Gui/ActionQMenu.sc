ActionQMenu{
	classvar padding = 36,<>buttonClass;
	var  <button,>arrowColor, <>defaultAction,<>globalAction, <value = "default",
	<>fontSize=15,<toolTip, >arrowSize=6.5,
	name, action, <menu;

	*new{|parent, bounds, name = "default",items,showArrow=true|
		^super.new.init(parent, bounds, name,items,showArrow);
	}

	init{|parent, bounds, nm, argItems, showArrow|
		bounds=bounds.asRect;
		name=nm;
		arrowColor = Color.grey(0.5);
		defaultAction={};
		globalAction={};
		this.makeButton(parent, bounds, name, showArrow);
		menu = Menu();
		argItems.do{|item|
			this.addItem(menu,item);
		};
		this;
	}

	makeButton{|parent, bounds, name,showArrow|
		var btnClass="Button", r,rr,l,bb;

		'RoundButton'.asClass.notNil.if{
			btnClass = "RoundButton";
		};
		btnClass = buttonClass ? btnClass;

		button = btnClass.asSymbol.asClass.new(parent, bounds)
		.states_([[name]]);
		bb=button.bounds.copy.moveTo(0,0);
		r = Rect(0,0,arrowSize,arrowSize);
		l=bb-r;
		rr=Rect(0,(l.height*0.5),arrowSize,arrowSize).right_(bb.right-arrowSize);

		(button.class.name == 'RoundButton').if{
			button.radius_(0).extrude_(true);
			showArrow.booleanValue.if{
				button.drawFunc_({this.pr_drawArrow(rr)});
			};
		};
		showArrow.booleanValue.and(btnClass=="Button" ).if{
			UserView(button,rr).drawFunc_({this.pr_drawArrow(r)});
		};

		button.action={menu.front}
	}

	toolTip_{arg str;
		button.toolTip_(str);
		button.refresh;
	}

	pr_drawArrow{|rect|
		var drawRect= rect??Rect(0,(button.bounds.height*0.5)-(arrowSize*0.5),arrowSize,arrowSize).right_(button.bounds.right-17);
		var penwidth = 2.max(drawRect.height*0.08);
		Pen.color= arrowColor;
		Pen.width=1;
		Pen.moveTo(drawRect.left@drawRect.top);
		Pen.lineTo(drawRect.center.x@drawRect.bottom);
		Pen.lineTo(drawRect.right@drawRect.top);
		Pen.lineTo(drawRect.left@drawRect.top);
		Pen.fill;
	}
	addItem{|menu,item|
		var action;
		action =this.prMakeMenuAction(item);
		action.notNil.if{
			menu.addAction(action);
		}
	}

	insertItem{|before,item|
		var action;
		action = this.prMakeMenuAction(item);

		action.notNil.if{

			menu.insertAction(before,action);
		}
	}
	prMakeMenuAction{|item|

		item.isKindOf(MenuAction).if{
			^item;
		};

		item.isKindOf(Association).if{
			^MenuAction(item.key).font_(Font.default.size_(fontSize))
			.action_({|a,b,c| item.value.value(item.key.asString,a,b,c);globalAction.value(item.key.asString,a,b,c); menu.visible_( false )});
		};

		(item.isKindOf(String) || item.isKindOf(Symbol)).if{

			item.asString.beginsWith("-").if{
				// item.removeAt(0);
				^MenuAction.separator.font_(Font.default.size_(fontSize));
			}{
				^this.prHandleString(item);
			};
		};

		Error("ActionMenu: item must be of type 'Association' (symbol->function),'MenuAction' or  'String' or 'Symbol' ").throw;

	}
	prHandleString{|item|

		^MenuAction(item.asString).font_(Font.default.size_(fontSize))
		.action_({|a,b,c| defaultAction.value(item.asString,a,b,c);globalAction.value(item.asString,a,b,c); menu.visible_( false )});

	}

	name_{|nm| button.states[0][0] = nm;button.refresh}

	name{button.states[0][0]}

	value_{|val| value = val;button.refresh}

	valueName_{|val|
		value = val;
		this.name_(val.asString);
		button.refresh;
	}

	valueIndexName_{|index|
		var action;
		action = menu.actions[index];
		value=action.string;
		this.name_(value.asString);
		button.refresh;
	}
}

PathActionQMenu : ActionQMenu{
	classvar <>defaultOpenAction,>defaultAllowedFiles,>defaultPreload=true;
	var  toplevel=true, toplevelarray,  <>openAction, <>allowedFiles,preload;

	*new{|parent, bounds, name = "New Menu",items,preload,showArrow=true|
		^super.new.init(parent, bounds, name,items,preload,showArrow);
	}

	init{|parent, bounds, nm,argItems,argPreload, showArrow|
		super.init(parent, bounds, nm, showArrow: showArrow);
		openAction=defaultOpenAction?{|string,action,what,val| [string,action,what,val].postln};
		preload=argPreload??defaultPreload;
		action={|path,modifier,buttonNum| path.value.value};
		allowedFiles = allowedFiles?defaultAllowedFiles;
		allowedFiles = allowedFiles?["rtf","rtfd","sc","scd","html","HTML","schelp"];
		argItems.do{|item|
			this.addItem(menu,item);
		};
	}
	addItem{|menu,item|
		var action;
		item.isKindOf(String).if{
			item.endsWith("*").if{
				item.escapeChar($ ).pathMatch.do{|str|
					this.addItem(menu,str);
				};
				^nil;
			}
		};
		action =this.prMakeMenuAction(item);
		action.notNil.if{
			menu.addAction(action);
		}
	}

	prHandleString{|item|
		var submenu, submenuAction, submenuAsAction;
		var pathName=PathName(item);
		pathName.isFolder.if{
			submenu = Menu().string_(pathName.folderName);
			preload.if{this.prPopulateSubmenu(submenu,item);}
			{
				submenuAction={|a,w| (w == \aboutToShow).if{this.prPopulateSubmenu(submenu,item,submenuAction)}};
				submenu.addDependant(submenuAction);
				submenu.onClose_({ submenu.removeDependant(submenuAction) });
			};
			submenuAsAction= submenu.asMenuAction.font_(Font.default.size_(fontSize));

			^submenuAsAction;
		};
		if(allowedFiles.occurrencesOf( pathName.extension ) > 0){
			^MenuAction(pathName.fileName).font_(Font.default.size_(fontSize))
			.action_({|a,b,c|item.postln; openAction.value(item);globalAction.value(item,a,b,c); menu.visible_( false )});
		};
		^nil;

	}

	prPopulateSubmenu{|submenu,item|
		var pathName=PathName(item);
		submenu.actions.size.booleanValue.not.if{
			pathName.entries.do{|path|
				this.addItem(submenu, path.fullPath);
			}
		}

	}

}

