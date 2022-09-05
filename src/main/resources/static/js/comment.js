const articlenum = document.querySelector("#articlenumHidden").innerHTML;
const loginUsernum = document.querySelector("#loginUsernum").innerHTML;
const commentBox = document.querySelector("#commentBox");

//댓글 작성
function commentWrite() {
    let token = getCsrfToken();
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
		let token = getCsrfToken();

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
        for (const comment of commentList) {
            let replyHtmlSource = ' ';
            let date = moment(comment.createdDate).format('YY-MM-DD HH:mm:ss');

            replyHtmlSource +=
				`<div class="mt-2 comment-box" id="commentBox">
					<div class="d-flex flex-row p-2">
						<div>
        					<img src="${comment.picUrl}" width="40" height="40" class="rounded-circle me-2">
					    </div>
					    <div class="w-100">
					        <div class="d-flex justify-content-between align-items-center">
					            <span class="me-2">${comment.username}</span>
					            <div class="d-flex flex-row align-items-center">
					                <small>${date}</small>`

            //로그인 유저와 댓글 작성 유저가 동일할 때 삭제 버튼 활성화
			if(loginUsernum == comment.usernum){
				replyHtmlSource += `<i id="deleteIcon" data-cnum="${comment.commentnum}" class="fas fa-trash ms-3"></i>`
			}//본래 span 태그로 i 태그 감싼 다음 이벤트는 span 클릭해서 실행시켰는데, 이러면 한번 실행하고 나서 span 태그 사라지고 i 태그가 target 바뀜

			replyHtmlSource += `</div>
					        </div>
					        <p class="text-justify comment-text mb-0">${comment.content}</p>`
			replyHtmlSource += `</div></div></div> `
			commentBox.innerHTML += replyHtmlSource;
		}//for
		addEvent()
	}//if
}//function

function addEvent(){ //댓글 클릭 이벤트 부여
	const iconBtn = document.querySelectorAll("#deleteIcon");
	for(const btn of iconBtn){
		btn.addEventListener("click", function(event){
			if(confirm("삭제하겠습니까?")){
				let token = getCsrfToken();
			    console.log("삭제 댓글 번호 " + event.target.dataset.cnum);

			    const xhr = new XMLHttpRequest();
	            xhr.open("POST", "/comment/delete?commentnum="+event.target.dataset.cnum+"&articlenum="+articlenum);
			    xhr.setRequestHeader("X-XSRF-TOKEN", token);
			    xhr.send();

			    xhr.onload = () => {
			        makeCommentBox(xhr, commentBox);
			    }
		    }
	    }, 20000)
	}
}
