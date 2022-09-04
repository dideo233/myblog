const viewer = toastui.Editor.factory({
    el: document.querySelector('#viewer'),
    viewer: true,
    height: '500px',
    initialValue: ''
});

const contents = document.querySelector("#contents");
console.log("test : " + contents.value);
viewer.setMarkdown(contents.value);
