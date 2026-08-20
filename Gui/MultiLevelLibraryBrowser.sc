MultiLevelLibraryBrowser{
    classvar   >defaultPrefix,>defaultSuffix;
	var currentItem=0, <view,attributes,<>autowidth=true,hspacing=1, <itemBuildFunction, <currentPath, <selectedIndices,
	<listViews,<>colWidth=200,<>fontSize=12,
	<dict, <startPath, <>action, <>dirPrefix="",<>dirSuffix=" ➡ ";

	*new{|title ="Library Browser", action, source|
		^super.new.init(title, action, source)
	}

	init{|title , a, source|
		var w;
		action = a;
		listViews=[];
		selectedIndices=[];
		attributes=IdentityDictionary(); // the attributes of the ListViews
		view = Window.new(title,Window.flipY(Rect(100,100,colWidth,400) ) ).front;
		view.layout_(GridLayout().hSpacing_(hspacing));
        dirPrefix=defaultPrefix?dirPrefix;
        dirSuffix=defaultSuffix?dirSuffix;

		source.notNil.if{this.load(source)};
	}

	load{|source ... startpath|
		startPath= startpath;
		dict=source;
		startPath.notNil.if{
			this.prAddLevel(0,dict.at(*startPath));
		}{
			this.prAddLevel(0,dict.dictionary);
		};
		{	this.prItemBuildAction(listViews[0], dict)}.defer(0.1); // allow custom handling of list attributes
	}
	reload{
		var pth = currentPath.copy, currenItemIndex, ind;
		pth.pop;

		startPath.notNil.if{
			this.prAddLevel(0,dict.at(*startPath));
		}{
			this.prAddLevel(0,dict.dictionary);
		};
		try{
			listViews.do{arg v,i;
				if(v.items.size>selectedIndices.at(i)){
					v.valueAction_(selectedIndices.at(i));
				}{
					if(v.items.size>0){
						v.valueAction_(selectedIndices.at(0));
					};

				};
			};
		};

	}

	prAddLevel{|i,currentItem|
		var fnt,lv,b;
		// make ListView at i , if it is not there yet
		listViews[i].isNil.if{
			// add the listview
			view.layout.add(lv=ListView().font_(Font.default.size_(fontSize)),0,i);
			(autowidth &&(i>0)).if{view.bounds=view.bounds.width_(view.bounds.width+colWidth+hspacing)};
			listViews=listViews.add(lv);

			// deletfunction
			//lv.keyUpAction = this.prMakeDeleteFunction(lv,i);
			this.prSetAttributes;
		};
		// make selectedIndices at i , if it is not there yet
		selectedIndices[i].isNil.if{
			selectedIndices=selectedIndices.add(0);
		};
		listViews[i].items=[];
		if(i==0){
			listViews[0].items=this.prGetItems(currentItem);
		};
		listViews[i].action_{|obj|
			var ind = i, str, item, currentKey;
			this.prClearBelow(ind);

			str = obj.items.at(obj.value);
			currentKey=listViews.at(i).items.at(obj.value);
			currentKey = this.prCleanKey(currentKey);
			item = currentItem.at(currentKey);
			if(str!="-"){
				if(item.isKindOf(IdentityDictionary)){
					this.prAddLevel(ind+1,item);
					listViews[ind+1].items = this.prGetItems(item);
					this.prItemBuildAction(listViews[ind+1],dict); // allow custom handling of list attributes
					this.prCurrentPath_(i);
				}{
					this.prCurrentPath_(i);
					action.value(this,currentPath, item);
				};
			};
			selectedIndices[i]=listViews[i].value;
            // selectedIndices.postln;

		};

	}

/*	prMakeDeleteFunction{|lv, i|
		^{|obj, char, mod, unicode, keycode, key|
			var ind=i;
			((key==16777219)||(key==16777223)).if{
				SCAlert("Really delete at "+currentPath.asString+"?",actions:[
					{"canceled".postln},
					{
						var items;
						items =obj.items;
						this.prCurrentPath_(ind);

						dict.removeAt(*currentPath);
						items.removeAt(obj.value);
						obj.items=items;
						this.reload;
					}
				]);
				//dict.at(*currentPath).postln;
				//currentPath.postln;
			}
		}
	}*/


	prCurrentPath_{|i|
		currentPath=[];
		(i+1).do{|n|
			var key;
			key = listViews[n].items[listViews[n].value];
			currentPath =currentPath.add(this.prCleanKey(key));
		};
		currentPath = startPath ++ currentPath;
        // currentPath.postln;
	}
	prCleanKey{|currentKey|
		var foundIndex;
		foundIndex = currentKey.asString.find(dirPrefix);
		(foundIndex==0).if{
			currentKey=currentKey.copyRange(dirPrefix.size,currentKey.size);
		};
		foundIndex = currentKey.asString.find(dirSuffix);
		foundIndex.notNil.if{
			currentKey=currentKey.copyRange(0,foundIndex-1);
		};
		^currentKey=currentKey.asSymbol;
	}

	prClearBelow{|k|
		listViews.do{|v,i|
			(i>k).if{
				v.items=[];
			};
		};
	}

	prGetItems{|d|
		var items=[];
		(d.isKindOf(Archive)||d.isKindOf(MultiLevelIdentityDictionary)).if{
			d=d.dictionary;
		};
		d.keysValuesDo{|key,item|
			item.isKindOf(IdentityDictionary).if{
				items=items.add(dirPrefix++key.asString++dirSuffix);
			}{
				items=items.add(key.asString);
			};
		};
		^["-"]++items.sort;
	}

	prItemBuildAction{|listView, currentItem|
		itemBuildFunction.notNil.if{
			itemBuildFunction.value(listView,currentItem, listView.items.collect{|item| this.prCleanKey(item)} );
		}
	}
	prSetAttributes{
		listViews.do{|view|
			attributes.keysValuesDo{|key,val|
				key=(key.asString++"_").asSymbol;
				view.tryPerform(key,val);
			}
		}
	}
	attributes_{|a|
		attributes=a;
		this.prSetAttributes;
	}
}
