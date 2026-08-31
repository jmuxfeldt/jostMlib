Debouncer {
    var func, wait, immediate, task;

    *new { |func, wait=0.1, immediate=false|
        ^super.newCopyArgs(func, wait, immediate).init
    }

    init {
        task = nil;
    }

    trigger { |...args|
        if(immediate) {
            if(task.isNil) {
                func.valueArray(args);
                task = Task {
                    wait.wait;
                    task = nil;
                }.play;
            };
        } {
            task !? { task.stop };
            task = Task {
                wait.wait;
                func.valueArray(args);
                task = nil;
            }.play;
        };
    }

    reset {
        task !? { task.stop };
        task = nil;
    }
}

DebounceCounter : Debouncer {
    var count, hits, storedArgs;

    *new { |func, wait=0.5, count=2, immediate=true|
        ^super.new(func, wait, immediate).prInitCount(count)
    }

    prInitCount { |n|
        count = n;
        hits = 0;
    }

    trigger { |...args|
        if(hits == 0) { storedArgs = args } { if(args != storedArgs) { hits = 0; storedArgs = args } };
        hits = hits + 1;
        task !? { task.stop };
        if(immediate) {
            if(hits >= count) {
                func.valueArray(storedArgs);
                hits = 0;
            };
            task = Task {
                wait.wait;
                hits = 0;
                task = nil;
            }.play;
        } {
            task = Task {
                wait.wait;
                if(hits >= count) { func.valueArray(storedArgs) };
                hits = 0;
                task = nil;
            }.play;
        };
    }

    reset {
        super.reset;
        hits = 0;
    }
}