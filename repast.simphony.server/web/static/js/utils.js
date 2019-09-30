export function empty(elem) {
    while (elem.lastChild) {
        elem.removeChild(elem.lastChild);
    }
}

export class DataViewWrapper {

    constructor(dataView, startOffset) {
        this.dv = dataView;
        this.offset = startOffset;
    }

    rewindInt32() {
        this.offset -= 4;
    }

    getInt32() {
        let v = this.dv.getInt32(this.offset);
        this.offset += 4;
        return v;
    }

    getFloat32() {
        let v = this.dv.getFloat32(this.offset);
        this.offset += 4;
        return v;
    }

    getFloat64() {
        let v = this.dv.getFloat64(this.offset);
        this.offset += 8;
        return v;
    }
}

// Takes a three.js loader and returns a promisified 
// version of it. This allows us to wait until the loader
// has successfully loaded (textures for example) before
// proceeding to something that needs whatever is loaded
// https://blackthread.io/blog/promisifying-threejs-loaders/
export function promisifyLoader(loader, onProgress) {
    function promiseLoader(url) {
        return new Promise( (resolve, reject) => {
            loader.load(url, resolve, onProgress, reject);
        });
    }

    return {
        originalLoader: loader,
        load: promiseLoader
    };
}
