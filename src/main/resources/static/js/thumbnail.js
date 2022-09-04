const thumbBox = document.getElementById("thumbBox");
const uploadThumbBtn = document.getElementById("thumbnail");
const thumbDel = document.getElementById("thumbDelBtn");
const previewThumb = document.getElementById("thumbnailPreView");
const thumbUrl = document.getElementById("thumbnailUrl")

function uploadThumbnailImg(input) {
    if(input.files && input.files[0]) {
        let token = null;//getCsrfToken();
        let formData = new FormData();
        formData.append('img', input.files[0]);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/article/uploadImg", false);
        xhr.setRequestHeader("contentType", "multipart/form-data");
        xhr.setRequestHeader("X-CSRF-TOKEN", token);
        xhr.send(formData);

        console.log('aasd');
        if (xhr.readyState === 4 && xhr.status === 200) {
            thumbUrl.value = xhr.response;

            const reader = new FileReader();
            reader.onload = e => {
                previewThumb.src = e.target.result;
            }

            thumbBox.style.display = ''
            reader.readAsDataURL(input.files[0])
        } else {
            alert("이미지가 정상적으로 업로드되지 못했습니다.")
        }
    }
}

uploadThumbBtn.addEventListener("change", e => {
    uploadThumbnailImg(e.target);
})

thumbDel.addEventListener("click", () =>{
    thumbBox.style.display = 'none';
    thumbUrl.value = "";
})
