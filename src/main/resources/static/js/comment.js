const commentBox = document.querySelector("#commentBox");
const articlenum = document.querySelector("#articlenumHidden").innerHTML;

//댓글 작성
function commentWrite() {
    let token = null;//getCsrfToken();
    let content = {content: document.querySelector("#commentContent").value};

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/comment/write?articlenum="+articlenum);
    xhr.setRequestHeader("content-type", "application/json");
    xhr.setRequestHeader("X-XSRF-TOKEN", token);
    xhr.send(JSON.stringify(content));

    xhr.onload = () => {
        makeCommentBox(xhr, commentBox);
        document.querySelector("#commentContent").value ='';
    }
}

//댓글 리스트 가져오기
document.addEventListener("DOMContentLoaded",
	function(){
		let token = null;//getCsrfToken();

	    const xhr = new XMLHttpRequest();
	    xhr.open("GET", "/comment/list/" + articlenum);
	    xhr.setRequestHeader("X-XSRF-TOKEN", token);
	    xhr.send();

	    xhr.onload = () => {
	        makeCommentBox(xhr, commentBox);
	    }
	}
);

//댓글 리스트 렌더링
function makeCommentBox(xhr, commentBox) {
    if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {
		let commentList = JSON.parse(xhr.response);

        // 초기화
        commentBox.innerHTML = ' ';
        // 댓글 리스트
        let idx = 1;
        for (const parentComment of commentList) {
            let replyHtmlSource = ' ';
            let date = moment(parentComment.createdDate).format('YY-MM-DD HH:mm:ss');

            replyHtmlSource +=
				`<div class="mt-2 comment-box" id="commentBox">
					<div class="d-flex flex-row p-2">
					    <span style="display:none" id="parentCommentnum" >${parentComment.commentnum}</span>
						<div>
        					<img src="${parentComment.picUrl}" width="40" height="40" class="rounded-circle me-2">
					    </div>
					    <div class="w-100">
					        <div class="d-flex justify-content-between align-items-center">
					            <span class="me-2">${parentComment.username}</span>
					            <div class="d-flex flex-row align-items-center">
					                <small>${date}</small>
					                <i class="fas fa-trash ms-3"></i>
					            </div>
					        </div>
					        <p class="text-justify comment-text mb-0">${parentComment.content}</p>`
			replyHtmlSource += `</div></div></div> `
			commentBox.innerHTML += replyHtmlSource;
		}//for
	}//if
}//function