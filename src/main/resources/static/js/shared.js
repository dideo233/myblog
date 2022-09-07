Kakao.init('5a527df409aef2f6edf4c84474597310');
    function shareKakao() {
    Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
            title: "title - Dideo'sLog",
            description: "카카오 공유하기 테스트",
            imageUrl:"https://raw.githubusercontent.com/dideo233/imageRepo/main/image/1e0aba4f-0de3-4adc-a749-712d03734b6c.png",
            link: {
                mobileWebUrl: window.location.href,
                webUrl: window.location.href,
            },
        },
        buttons: [
            {
                title: '웹으로 보기',
                link: {
                    mobileWebUrl: window.location.href ,
                    webUrl: window.location.href,
                },
            }
        ],
    })
}