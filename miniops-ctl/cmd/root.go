package cmd

import (
    "fmt"
    "github.com/spf13/cobra"
)

const (
    AppName = "opsctl"
)

var (
    rootCmd *cobra.Command
)

func init() {
    rootCmd = &cobra.Command{
        Use:   AppName,
        Short: "A command line tool for managing miniops resources",
        Long:  "A command line tool for managing miniops resources",
        RunE:  run,
    }

    // basic cmd
    rootCmd.AddCommand(versionCmd, infoCmd)
}

func Execute() {
    if err := rootCmd.Execute(); err != nil {
        panic(err)
    }
}

func run(*cobra.Command, []string) error {
    fmt.Println("Running " + AppName)
    return nil
}
