let articleTitle = document.querySelector("#articleTitle").innerHTML;
let articleThumbnailUrl = document.querySelector("#articleThumbnailUrl").getAttribute("src");

Kakao.init('5a527df409aef2f6edf4c84474597310');
    function shareKakao() {
    console.log(articleTitle);
    console.log(articleThumbnailUrl);
    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: articleTitle + "- Dideo'sBlog",
            imageUrl:articleThumbnailUrl,
            link: {
                mobileWebUrl: window.location.href,
                webUrl: window.location.href,
            },
        },
        buttons: [
            {
                title: '웹으로 보기',
                link: {
                    mobileWebUrl: window.location.href,
                    webUrl: window.location.href,
                },
            }
        ],
    })
}