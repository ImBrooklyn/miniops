package util

func GetOrDefault[T any](m map[string]any, key string, defaultVal T) T {
    if v, ok := m[key]; ok {
        if t, okk := v.(T); okk {
            return t
        }
    }
    return defaultVal
}
