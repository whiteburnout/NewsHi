import { createInstance, createFileInstance } from './index.js';

const instance = createInstance();
// const config = {
//   headers: { "access-token": localStorage.getItem("access-token") }
// };

const fileInstance = createFileInstance();

function boardList(id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('/boardList', {
      params: {
        id: id,
      },
    })
    .then(success)
    .catch(fail);
}

function uploadImage(image, success, fail) {
  fileInstance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  const form = new FormData();
  form.append('file', image);

  fileInstance
    .post('/uploadFile', form)
    .then(success)
    .catch(fail);
}

function boardInsert(board, id, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  const list = [
    {
      title: board.title,
      content: board.content,
    },
    {
      id: id,
    },
  ];
  // [
  //   {
  //     "title": "새로운 큐레이터",
  //     "content": "<p>안녕하세요 반가워요</p>"
  //   },
  //   {
  //     "id": "chunawoos@hanmail.net"
  //   }
  // ]
  instance
    .post('/boardInsert', list)
    .then(success)
    .catch(fail);
}

function boardUpdate(b, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .put('/boardUpdate', b)
    .then(success)
    .catch(fail);
}

function boardDelete(boardno, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  var params = new URLSearchParams();
  params.append('boardPostNo', boardno);
  instance
    .delete('/boardDelete', {
      params: params,
    })
    .then(success)
    .catch(fail);
}

function boardCommentList(boardPostNo, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  instance
    .get('/boardCommentList', {
      params: {
        boardPostNo: boardPostNo,
      },
    })
    .then(success)
    .catch(fail);
}

function boardCommentInsert(comment, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  var frm = new FormData();
  frm.append('boardPostNo', comment.boardPostNo);
  frm.append('id', comment.id);
  frm.append('content', comment.content);

  instance
    .post('/boardCommentInsert', frm)
    .then(success)
    .catch(fail);
}

function boardCommentUpdate(comment, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );

  const bc = {
    commentNo: comment.CommentNo,
    boardPostNo: comment.BoardPostNo,
    userNo: comment.UserNo,
    content: comment.Content,
    date: comment.Date,
  };

  instance
    .put('/boardCommentUpdate', bc)
    .then(success)
    .catch(fail);
}

function boardCommentDelete(commentNo, success, fail) {
  instance.defaults.headers['access-token'] = window.localStorage.getItem(
    'access-token'
  );
  var params = new URLSearchParams();
  params.append('commentNo', commentNo);
  instance
    .delete('/boardCommentDelete', {
      params: params,
    })
    .then(success)
    .catch(fail);
}

export {
  boardList,
  boardInsert,
  boardUpdate,
  boardDelete,
  boardCommentList,
  boardCommentDelete,
  boardCommentInsert,
  boardCommentUpdate,
  uploadImage,
};
