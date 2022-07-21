const getLocalStorageItem = (key: string) => {
  const value = localStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const getSessionStorageItem = (key: string) => {
  const value = sessionStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

// TODO: <T>에 default 값을 넣어주는 건 어떨까?
const saveLocalStorageItem = <T>(key: string, value: T | Array<T>) =>
  localStorage.setItem(key, JSON.stringify(value));

const saveSessionStorageItem = <T>(key: string, value: T | Array<T>) =>
  sessionStorage.setItem(key, JSON.stringify(value));

const removeLocalStorageItem = (key: string) => localStorage.removeItem(key);

export {
  getLocalStorageItem,
  saveLocalStorageItem,
  getSessionStorageItem,
  saveSessionStorageItem,
  removeLocalStorageItem
};
