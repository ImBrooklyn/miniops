package cmd

import (
    "fmt"
    "github.com/spf13/cobra"
)

var (
    versionCmd = &cobra.Command{
        Use:   "version",
        Short: "Print version/build info",
        Long:  "Print version/build information",
        Run: func(*cobra.Command, []string) {
            fmt.Println("Version:", "0.0.1")
        },
    }

    infoCmd = &cobra.Command{
        Use:   "info",
        Short: "Print version/build info",
        Long:  "Print version/build information",
        Run: func(*cobra.Command, []string) {
            fmt.Println("Version:", "0.0.1")
        },
    }
)
