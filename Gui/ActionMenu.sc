ActionMenu{
    classvar padding = 36,<>buttonClass;
    var <>items, listview, modifier, <button, <>defaultAction,<>unfocusClose = true,<>globalAction, winPosition, <value = "default",
    <>closeOnSelect=true, <>fontSize=15,<toolTip,
    allowedFiles,name,level=0, action, window;

    *new{|parent, bounds, name = "default",items,showArrow=true|
        ^super.new.init(parent, bounds, name,items,showArrow);
    }

    init{|parent, bounds, nm, argItems, showArrow|
        bounds=bounds.asRect;
        items=[];
        name=nm;
        this.prAddSpacer;
        this.makeButton(parent, bounds, name, showArrow);
        // the action performed by the list
        this.pr_makeAction;
        // the action performed by the list when not as symbol->function association
        defaultAction={|val| ("you chose: "+val).postln};
        globalAction={};
        argItems.do{|item|
            this.prAddItem(item);
        };
    }
/*    asView{
        ^button.asView;
    }*/

    makeButton{|parent, bounds, name,showArrow|
        var btnClass="Button";
        Class.findAllReferences('RoundButton').notNil.if{
            btnClass = "RoundButton";
        };
        btnClass = buttonClass ? btnClass;
        showArrow.booleanValue.and(btnClass=="Button" ).if{ name = name++" ⌄"};

        button = btnClass.interpret.new(parent, bounds)
        .states_([[name,Color.black,Color.white.alpha_(0.3)]]);
        (button.class.name == 'RoundButton').or(button.class.name == 'SmoothButton').if{
            button.radius_(0)
            .background_(Color.clear).extrude_(true);
            showArrow.booleanValue.if{
                button.drawFunc_{
                    Pen.color=Color.grey(0.5);
                    Pen.drawIcon(\down,Rect(bounds.width-14,0,10,bounds.height))
                };
            };
        };
        button.action=this.prButtonAction;
    }
    toolTip_{arg str;
        button.toolTip_(str);
        button.refresh;
    }

    pr_makeAction{
        action={arg l;
            l.value.isKindOf(Function).if{
                l.value.value(this,modifier); // do function at key
            };
            l.value.isKindOf(Association).if{
                l.value.value(this,modifier); // do function at key
            };
            (l.value.isKindOf(String) || l.value.isKindOf(Symbol)).if{
                (l.value!="-").if{// do defaultFunction at key with arg l.value
                    defaultAction.value(l.value);
                }
            };
            globalAction.value(l.value);

        };
    }

    prButtonAction{
        ^{var listitems, winbounds;
            listitems=items.collect{|item| // populate list
                item.isKindOf(Association).if{
                    item.key.asString;
                }{
                    (item.isKindOf(String) || item.isKindOf(Symbol)).if{
                        item;
                    }{
                        "wrong type in items".error;
                    }
                }
            };
            (level==0).if{
                this.prSetWinPosition;
            };
            winbounds =  this.prGetWinBounds(listitems);
            this.prMakeListView(listitems, winbounds);
        };
    }

    prMakeListView{| listitems, winbounds |
        var escFunc;
        window = Window.new(name).front.bounds_(winbounds).alwaysOnTop_(true);


        listview  =  ListView.new(window.view,window.view.bounds);
        listview.resize_(5).items_(listitems);//.focus(1);
        listview.action= {arg l; action.value(items[l.value])};
        closeOnSelect.if{listview.mouseUpAction_({window.close})};

        listview.mouseDownAction={arg v,x,y,mod; modifier = mod};
        listview.font_(Font.default.size_(fontSize));
        unfocusClose.if{
            {0.01.wait;window.endFrontAction_({window.close})}.fork(AppClock);
        };
        escFunc =  { arg view,char,modifier,unicode,keycode;
            (keycode == 53).if{window.close()};

        };
        View.globalKeyDownAction=View.globalKeyDownAction.addFunc(escFunc);
        window.onClose_{View.globalKeyDownAction.removeFunc(escFunc);button.focus(0)};


    }

    prAddSpacer{
        items=items.add('-'->{});
    }

    prAddItem{|item|

        (item.isKindOf(Association) || item.isKindOf(String) || item.isKindOf(Symbol)).if{
            items=items.add(item);
        }{
            "ActionMenu: item must be of type 'Association' (symobol->function) or  'String' or 'Symbol' ".error;
        }

    }

    prSetWinPosition{	|l|
        winPosition = GUI.cursorPosition;
        winPosition.y=winPosition.y+20;
    }

    prGetWinBounds{|l|
        var bounds, lHeight,lWidth, maxHeight, listHeight, listWidth, maxWidth;
        maxWidth = 0;
        bounds=Rect();
        l.do{|p| maxWidth=maxWidth.max(p.asString.size)}; // find the longest
        maxHeight=(40+(l.size*(fontSize+3)));
        lWidth = 100+(maxWidth*5);
        lHeight = maxHeight;
        lHeight = lHeight.min(Window.screenBounds.height-200);
        lWidth = lWidth.max(140);
        bounds.width =lWidth;
        bounds.height = lHeight;
        bounds.left=winPosition.x;
        bounds.top=Window.screenBounds.height-lHeight-winPosition.y;
        ^ bounds;
    }

    name_{|nm| button.states[0][0] = nm;button.refresh}
    name{button.states[0][0]}
    value_{|val| value = val;button.refresh}
    valueName_{|val|
        value = val;
        this.name_(val.asString);
        button.refresh;
    }
    valueIndexName_{|val|
        if( items[val].class==Event){
            val = items[val].key;
        }{
            val = items[val];
        };
        value=val;
        this.name_(val.asString);
        button.refresh;
    }


}

PathActionMenu : ActionMenu{
    classvar <>defaultOpenAction,<>defaultAllowedFiles;
    classvar   >defaultPrefix,>defaultSuffix;
    var  toplevel=true, toplevelarray, <returnpaths, <>openAction, <>allowedFiles, <>dirPrefix="",<>dirSuffix="/ ";


    *new{|parent, bounds, name = "New Menu",items,showArrow=true|
        ^super.new.init(parent, bounds, name,items, showArrow);
    }

    init{|parent, bounds, nm,newitems, showArrow|
        super.init(parent, bounds, nm, showArrow: showArrow);
        openAction=defaultOpenAction?{|path| path.postln};
        returnpaths=[];
        items=[];
        dirPrefix = defaultPrefix?dirPrefix;
        dirSuffix = defaultSuffix?dirSuffix;
        action={|val| val.value.value};
        allowedFiles = defaultAllowedFiles?["rtf","rtfd","sc","scd","html","HTML","schelp"];
        this.prAddSpacer;
        newitems.do{|item|
            this.prAddItem(item);
        };

    }

    prAddItem{|item|
        item.isKindOf(Association).if{
            items=items.add(item);
        }{
            item.isKindOf(String).if{
                (item=="-" ).if{this.prAddSpacer;}{
                    item.escapeChar($ ).pathMatch.do{|item|
                        var newitem;
                        newitem = this.prPathItem(item);
                        newitem.notNil.if{items=items.add(newitem)};
                        PathName( item ).isFolder.if{returnpaths=returnpaths.add(this.prPathUp(item));};
                    }
                }
            }{
                (item.class.name.asString != "True").if{
                    ("PathActionMenu: item"+item.class.name.asString+" must be of type 'Association' or 'String' ").error;

                };

            }
        };
        toplevelarray=items.copy;
    }
    prButtonAction{
        ^{
            level=0;
            items=toplevelarray.copy;
            this.prSetWinPosition;
            this.prRefreshAction;
        };
    }
    prRefreshAction{
        var l, winbounds;
        l=items.collect{|item| item.key.asString};
        winbounds= this.prGetWinBounds(l);
        this.prMakeListView(l, winbounds);

    }

    prPathItem{|path, key|
        PathName(path).isFile.if{
            ^ this.prFileItem(path)
        };
         PathName(path).isFolder.if{
            ^ this.prFolderItem(path, key)
        };
        ^nil;
    }

    prFileItem{|path|
        var key,action, marker;
        marker = "";
        key = (marker++path.basename);
        if(allowedFiles.occurrencesOf( key.splitext.at(1) ) > 0){
            action = {
                level=0;
                winPosition=nil;
                items=toplevelarray.copy;
                openAction.value(path);

            }
            ^ key.asSymbol->action;
        };
        ^ nil;
    }
    prFolderItem{|path,key|
        var action;
        key = key ? (dirPrefix++path.basename++dirSuffix).asSymbol;
        action = {{
            var pathname=PathName(path);
            window.endFrontAction_({});
            unfocusClose.if{
                0.01.wait;
                window.close;
            };
            window=nil;
            listview=nil;
            items=[];
            this.prAddSpacer;
            (key!='../').if{
                level=level+1;
            }{
                level=level-1;
            };
            (level < 1).if{
                items = toplevelarray.copy;
            }{
                items=items.add(this.prPathItem(this.prPathUp(path),'../'));
                pathname.entries.do{|entry|
                    var newitem;
                    newitem = this.prPathItem(entry.absolutePath);
                    newitem.notNil.if{
                        items=items.add(newitem);
                    };
                };
            };



            this.prRefreshAction;
        }.fork(AppClock);
        };


        ^ key->action;
    }
    prPathUp{|path|
        if(path!="/"){
            (path.last == $/).if{
                path=path.copyRange(0, path.size-2);
            };
            path.do{
                if(path.last == $/){
                    ^ path;
                };
                path=path.copyRange(0, path.size-2);
            };
        };
        ^path;
    }

}

