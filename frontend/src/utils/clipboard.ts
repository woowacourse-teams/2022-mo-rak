// TODO: writeClipboard로 모듈 변경하기
const writeClipboard = (value: string) => navigator.clipboard.writeText(value);

export { writeClipboard };
