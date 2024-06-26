//에디터 글 옵션
const contents = document.querySelector("#content");
toastui.Editor.setLanguage(['ko', 'ko-KR'], {
    Markdown: '마크다운',
    WYSIWYG: '일반',
    Write: '편집하기',
    Preview: '미리보기',
    Headings: '제목크기',
    Paragraph: '본문',
    Bold: '굵게',
    Italic: '기울임꼴',
    Strike: '취소선',
    Code: '인라인 코드',
    Line: '문단나눔',
    Blockquote: '인용구',
    'Unordered list': '글머리 기호',
    'Ordered list': '번호 매기기',
    Task: '체크박스',
    Indent: '들여쓰기',
    Outdent: '내어쓰기',
    'Insert link': '링크 삽입',
    'Insert CodeBlock': '코드블럭 삽입',
    'Insert table': '표 삽입',
    'Insert image': '이미지 삽입',
    Heading: '제목',
    'Image URL': '이미지 주소',
    'Select image file': '이미지 파일을 선택하세요.',
    'Choose a file': '파일 선택',
    'No file': '선택된 파일 없음',
    Description: '설명',
    OK: '확인',
    More: '더 보기',
    Cancel: '취소',
    File: '파일',
    URL: '주소',
    'Link text': '링크 텍스트',
    'Add row to up': '위에 행 추가',
    'Add row to down': '아래에 행 추가',
    'Add column to left': '왼쪽에 열 추가',
    'Add column to right': '오른쪽에 열 추가',
    'Remove row': '행 삭제',
    'Remove column': '열 삭제',
    'Align column to left': '열 왼쪽 정렬',
    'Align column to center': '열 가운데 정렬',
    'Align column to right': '열 오른쪽 정렬',
    'Remove table': '표 삭제',
    'Would you like to paste as table?': '표형태로 붙여 넣겠습니까?',
    'Text color': '글자 색상',
    'Auto scroll enabled': '자동 스크롤 켜짐',
    'Auto scroll disabled': '자동 스크롤 꺼짐',
    'Choose language': '언어 선택',
})

//에디터 설정
const editor = new toastui.Editor({
    el: document.querySelector('#editor'),
    height: '800px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    language: 'ko',
    toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr'],
        ['ul', 'ol'],
        ['code'],
        ['table', 'image', 'link']
    ],
    hooks: {
        addImageBlobHook: (blob, callback) => { //이미지 업로드 로직 커스텀. base64로 통쨰로 DB 담기는 것을 경로만 저장하도록 수정
            let imgurl = uploadImage(blob);
            callback(imgurl, "첨부 이미지"); //editor로 콜백됨
        }
    }
});
//에디터 반응형
 editorMobile = new toastui.Editor({
    el: document.querySelector('#editorMobile'),
    height: 'auto',
    language: 'ko',
    initialEditType: 'wysiwyg',
    previewStyle: 'tab',
    toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr'],
        ['ul', 'ol'],
        ['code']
    ]
});

editor.setMarkdown(contents.value);
editorMobile.setMarkdown(contents.value);

//이미지 업로드 설정
function uploadImage(blob) {
    let csrfToken = getCsrfToken();
    let formData = new FormData();
    formData.append('img', blob);

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/article/uploadImg", false);
    xhr.setRequestHeader("contentType", "multipart/form-data");
    xhr.setRequestHeader("X-XSRF-TOKEN", csrfToken);
    xhr.send(formData);

    if (xhr.readyState === 4 && xhr.status === 200) {
        return xhr.response;
    } else {
        alert("이미지가 정상적으로 업로드되지 못했습니다.")
    }

}
function postArticle() {
    contents.value = editor.getMarkdown();
    if (!checkArticleValidate()) {return;}
    document.querySelector("#articleFrm").submit();
}

//유효성 체크
function checkArticleValidate() {
    let title = document.querySelector("#title");
    let category = document.querySelector("#category");
    if (category.value === ""){
        alert("카테고리를 입력하세요")
        return false;
    } else if (title.value === "") {
        alert("제목을 입력하세요")
        return false;
    } else if(contents.value === ""){
        alert("내용을 입력하세요")
        return false;
    } else if(contents.value.length >=  65535 ){
        alert("내용은 65535 미만 용량으로 입력하세요");
        return false;
    }

    return true;
}

// 자동 저장 기능
function autoSave(){
    let token = getCsrfToken();
    let tempDto = {};
    tempDto.content = editor.getMarkdown();
    console.log("auto save...");

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/article/temp/autoSave")
    xhr.setRequestHeader("content-type", "application/json");
    xhr.setRequestHeader("X-XSRF-TOKEN", token);
    xhr.send(JSON.stringify(tempDto));

    xhr.onload = () => {
        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {
            console.log("autoSave");
        }
    }
}

setInterval("autoSave()", 60000); //1분 간격 자동 저장

//작성 중 게시글 호출
window.onload = function () {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "/article/temp/getTemp")
    xhr.send();

    xhr.onload = () => {
        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {
          let tmp = JSON.parse(xhr.response);
            if(tmp.content != null&& tmp.content != ''){
                let isLoadTmp = confirm("이전에 작성하던 글이 있습니다. 이어서 작성하시겠습니까?");
                if(isLoadTmp){
                    editor.setMarkdown(tmp.content);
                }
            }
        }
    }
}