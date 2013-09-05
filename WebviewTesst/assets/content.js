rt = window.external.mxGetRuntime();
browser = rt.create("mx.browser");
rt.listen("inject", inject);
function inject(s) {
	browser.executeScript(s);
}
