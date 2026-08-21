MultiLevelLibraryBrowser{
    classvar   >defaultPrefix="",>defaultSuffix=" ➡ ";

	var currentItem=0, <view,<attributes,<>autowidth=true,hspacing=1, <>itemBuildFunction, <currentPath, <selectedIndices,
	<listViews,<>colWidth=200,
	<dict, startPath, action, <>dirPrefix,dirSuffix;

	*new{|title ="Library Browser", action, dict, startPath|
		^super.new.init(title, action, dict, startPath )
	}

	init{|title , a, d, sp|
		var w;
		action = a;
		listViews=[];
		selectedIndices=[];
		attributes=IdentityDictionary(); // the attributes of the ListViews
		view = Window.new(title,Window.flipY(Rect(100,100,colWidth,400) ) ).front;
		view.layout_(GridLayout().hSpacing_(hspacing));
        dirPrefix=dirPrefix?defaultPrefix;
        dirSuffix=dirSuffix?defaultSuffix;

		d.notNil.if{this.load(d)};
	}

	load{|dictionary ... args|
		startPath= args;
		dict=dictionary;
		startPath.notNil.if{
			this.prAddLevel(0,dict.at(*startPath));
		}{
			this.prAddLevel(0,dict.dictionary);
		};
		{	this.prItemBuildAction(listViews[0], dict)}.defer(0.1);
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
		listViews[i].isNil.if{
			var lv,b;
			view.layout.add(lv=ListView(),0,i);
			(autowidth &&(i>0)).if{view.bounds=view.bounds.width_(view.bounds.width+colWidth+hspacing)};
			listViews=listViews.add(lv);
			this.prSetAttributes;
		};
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
					this.prItemBuildAction(listViews[ind+1],dict);
					this.prCurrentPath_(i);
				}{
					this.prCurrentPath_(i);
					action.value(this,currentPath, item);
				};
			};
			selectedIndices[i]=listViews[i].value;

		};

	}

	prCurrentPath_{|i|
		currentPath=[];
		(i+1).do{|n|
			var key;
			key = listViews[n].items[listViews[n].value];
			currentPath =currentPath.add(this.prCleanKey(key));
		};
		currentPath = startPath ++ currentPath;
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
                v.refresh;
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
