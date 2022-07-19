const getLocalStorageItem = (key: string) => {
  const value = localStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const getSessionStorage = (key: string) => {
  const value = sessionStorage.getItem(key);

  return value ? JSON.parse(value) : null;
};

const saveLocalStorageItem = <T>(key: string, value: T | Array<T>) =>
  localStorage.setItem(key, JSON.stringify(value));

const saveSessionStorageItem = <T>(key: string, value: T | Array<T>) =>
  sessionStorage.setItem(key, JSON.stringify(value));

export { getLocalStorageItem, saveLocalStorageItem, getSessionStorage, saveSessionStorageItem };
