const articlenum = document.querySelector("#articlenumHidden").innerHTML;
const loginUsernum = document.querySelector("#loginUsernum").innerHTML;
const commentBox = document.querySelector("#commentBox");

//댓글 작성 (부모 계층 : 0)
function commentWrite() {
    let token = getCsrfToken();
    let content = {content: document.querySelector("#commentContent").value};

    if(!checkCommentValidate()) {return;}

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/comment/write?articlenum="+articlenum);
    xhr.setRequestHeader("content-type", "application/json");
    xhr.setRequestHeader("X-XSRF-TOKEN", token);
    xhr.send(JSON.stringify(content));

    xhr.onload = () => {
        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {
            makeCommentBox(xhr, commentBox);
            document.querySelector("#commentContent").value ='';
        } else{
            alert("댓글 작성 중 에러 발생");
        }
    }
}

//자식 댓글 작성 (자식 계층 : 1)
function subCommentWrite(parentCommentnum, idx) {
    let token = getCsrfToken();
    let content = {content: document.querySelector("#child-content-"+idx).value};

    //checkCommentValidate()

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/comment/write?parentCommentnum="+parentCommentnum+"&articlenum="+articlenum);
    xhr.setRequestHeader("content-type", "application/json");
    xhr.setRequestHeader("X-XSRF-TOKEN", token);
    xhr.send(JSON.stringify(content));

    xhr.onload = () => {
        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {
            makeCommentBox(xhr, commentBox);
            document.querySelector("#commentContent").value ='';
        } else{
            alert("댓글 작성 중 에러 발생");
        }
    }
}


//유효성 체크
function checkCommentValidate() {
    let commentContent = document.querySelector("#commentContent");
    if (commentContent.value === ""){
        alert("내용을 입력하세요")
        return false;
    } else if(contents.value.length >=  65535 ){
        alert("내용은 250자 미만 입력하세요");
        return false;
    }
    return true;
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

        //자식 댓글폼 번호 ()
        let idx = 1;
        // 초기화
        commentBox.innerHTML = ' ';
        //부모 댓글 리스트
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
                                    //자식 댓글이 없으면서 로그인 유저와 댓글 작성 유저가 동일할 때 삭제 버튼 활성화
                                    if(loginUsernum == comment.usernum && comment.commentVOs.length == 0){
                                        replyHtmlSource += `<i id="deleteIcon" data-cnum="${comment.commentnum}" class="fas fa-trash ms-3"></i>`
                                    }//본래 span 태그로 i 태그 감싼 다음 이벤트는 span 클릭해서 실행시켰는데, 이러면 한번 실행하고 나서 span 태그 사라지고 i 태그가 target 바뀜
			replyHtmlSource += `</div>
					        </div>
					        <p class="text-justify comment-text mb-0">${comment.content}</p>`

			//자식 댓글 작성폼 호출 버튼 (로그인 했을 시)
			if(loginUsernum != -1){
            replyHtmlSource +=
                `<div class="comment-reply">
                    <div style="background-color: #ebebeb;">
                        <button class="btn align-items-center rounded collapsed" data-bs-toggle="collapse" data-bs-target="#comment-reply-${idx}" aria-expanded="false">
                            <i class="fa fa-comments-o me-2"></i> 대댓글
                        </button>
                    </div>
                </div>`
            }
                //자식 댓글 작성폼
                replyHtmlSource +=
                `<div class="collapse" id="comment-reply-${idx}">
                    <div class="ms-2 me-2 mt-3 d-flex align-items-center">
                        <textarea type="text" id="child-content-${idx}" name="child-content-${idx}" class="form-control" placeholder="대댓글을 작성하세요"></textarea>
                    </div>
                    <div class="d-flex">
                        <div class="ms-auto mt-2 me-2">
                            <button class="btn btn-secondary btn-sm" onclick="subCommentWrite(${comment.commentnum}, ${idx++})">등 록</button>
                        </div>
                    </div>
                </div>`

			//자식 댓글 리스트
            if(comment.commentVOs.length != 0){
                for(const childComment of comment.commentVOs){
                    console.log(childComment.commentnum);
                    let cDate = moment(childComment.createdDate).format('YY-MM-DD HH:mm:ss');
                    replyHtmlSource +=
                        `<div class="d-flex flex-row p-2">
                            <div>
                            <img src="${childComment.picUrl}" width="40"
                              height="40" class="rounded-circle me-2">
                            </div>
                            <div class="w-100">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="me-2">${childComment.username}</span>
                                    <div class="d-flex flex-row align-items-center">
                                        <small>${cDate}</small>`
                                        if(loginUsernum == childComment.usernum){
                                            replyHtmlSource += `<i id="deleteIcon" data-cnum="${childComment.commentnum}" class="fas fa-trash ms-3"></i>`
                                        }
                        replyHtmlSource += `</div>
                                </div>
                                <p class="text-justify comment-text mb-0">${childComment.content}</p>
                            </div>
                        </div>`
                    }
            }

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
