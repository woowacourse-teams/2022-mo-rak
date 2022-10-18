type CreateRange = {
  size: number;
  startNumber?: number;
  isReverse?: boolean;
};

const createRange = ({ size, startNumber = 0, isReverse = false }: CreateRange): Array<number> => {
  const sizedArray = Array.from({ length: size });

  if (isReverse) return sizedArray.map((_, idx) => startNumber - idx);

  return sizedArray.map((_, idx) => idx + startNumber);
};

export { createRange };
