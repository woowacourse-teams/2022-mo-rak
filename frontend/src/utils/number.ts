interface CreateRangeInterface {
  size: number;
  startNumber?: number;
  isReverse?: boolean;
}

// TODO: createRange로 모듈 만들기
const createRange = ({
  size,
  startNumber = 0,
  isReverse = false
}: CreateRangeInterface): Array<number> => {
  const sizedArray = Array.from({ length: size });

  if (isReverse) return sizedArray.map((_, idx) => startNumber - idx);

  return sizedArray.map((_, idx) => idx + startNumber);
};

export { createRange };
