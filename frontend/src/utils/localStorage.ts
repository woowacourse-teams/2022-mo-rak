const getLocalStorageItem = (key: string) => {
  const value = localStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const saveLocalStorageItem = <T>(key: string, value: T | Array<T>) =>
  localStorage.setItem(key, JSON.stringify(value));

export { getLocalStorageItem, saveLocalStorageItem };
