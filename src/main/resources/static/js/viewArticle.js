const viewer = toastui.Editor.factory({
    el: document.querySelector('#viewer'),
    viewer: true,
    height: '800px',
    initialValue: ''
});

const contents = document.querySelector("#contents");
console.log("test : " + contents.value);
viewer.setMarkdown(contents.value);

//게시글 삭제
function deleteArticle(){
	if(confirm("삭제하겠습니까?")){
		document.querySelector("#deleteFrm").submit();
	}
}
